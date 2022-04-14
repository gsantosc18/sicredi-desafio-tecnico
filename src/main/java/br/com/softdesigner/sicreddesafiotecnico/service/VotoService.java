package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import br.com.softdesigner.sicreddesafiotecnico.converter.VotoConverver;
import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.exception.*;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.UNABLE_TO_VOTE;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class VotoService {
    private final VotoRepository votoRepository;
    private final UserClient userClient;
    private final AssociadoService associadoService;
    private final SessaoService sessaoService;

    @Autowired
    public VotoService(VotoRepository votoRepository,
                       UserClient userClient,
                       AssociadoService associadoService,
                       SessaoService sessaoService) {
        this.votoRepository = votoRepository;
        this.userClient = userClient;
        this.associadoService = associadoService;
        this.sessaoService = sessaoService;
    }

    public Mono<VotoDTO> createVoto(CreateVotoDTO createVotoDTO) {
        final String document = createVotoDTO.getCpf();
        final String sessaoId = createVotoDTO.getSessao();

        return userClient.findCpf(document)
            .flatMap(userStatusDTO-> validateAndProcessAssociado(createVotoDTO, document, sessaoId, userStatusDTO))
            .switchIfEmpty(Mono.error(new InvalidDocumentException()))
            .doOnError(throwable -> Mono.error(new InvalidDocumentException()));
    }

    private Mono<VotoDTO> validateAndProcessAssociado(CreateVotoDTO createVotoDTO, String document, String sessaoId, UserStatusDTO userStatusDTO) {
        if (UNABLE_TO_VOTE.equals(userStatusDTO.getStatus())) {
            log.info("M=createVoto, message=O usuário não está apto para votar");
            return Mono.error(new UserUnableToVoteException());
        }
        return getOrCreateAssociadoByCpf(document)
                .flatMap(associado -> processAssociado(createVotoDTO, sessaoId, associado));
    }

    private Mono<VotoDTO> processAssociado(CreateVotoDTO createVotoDTO, String sessaoId, AssociadoDocument associado) {
        return sessaoService.findByIdDocument(sessaoId)
                .flatMap(session -> validateAndProcessAssociado(createVotoDTO, associado, session));
    }

    private Mono<VotoDTO> validateAndProcessAssociado(CreateVotoDTO createVotoDTO, AssociadoDocument associado, SessaoDocument session) {
        if (session.getTime().isBefore(now())) {
            log.info("M=createVoto, message=A sessão informada já foi encerrada");
            return Mono.error(new ViolateTimeSessionException());
        }

        return votoRepository.findByAssociadoAndSessao(associado, session)
            .hasElement()
            .flatMap(hasAssociadoSessao -> doCreateVote(createVotoDTO, associado, session, hasAssociadoSessao))
            .switchIfEmpty(Mono.error(new InvalidSessionException()));
    }

    private Mono<VotoDTO> doCreateVote(CreateVotoDTO createVotoDTO, AssociadoDocument associado, SessaoDocument session, Boolean hasAssociadoSessao) {
        if (hasAssociadoSessao) {
            log.info("M=doCreateVote, message=Já existe registro de voto do associado.");
            return Mono.error(new UserVoteAlreadyExistException());
        }
        return createVoto(createVotoDTO, session, associado);
    }

    private Mono<VotoDTO> createVoto(CreateVotoDTO createVotoDTO, SessaoDocument sessao, AssociadoDocument associadoDocument) {
            final VotoDocument votoDocument = new VotoDocument(associadoDocument, sessao, createVotoDTO.getVoto());
            return votoRepository.save(votoDocument).map(VotoConverver::toDTO);
    }

    private Mono<AssociadoDocument> getOrCreateAssociadoByCpf(String document) {
        return associadoService.findOrCreateAssociadoByCpf(document);
    }
}
