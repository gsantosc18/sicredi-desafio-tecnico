package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import br.com.softdesigner.sicreddesafiotecnico.converter.VotoConverver;
import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import br.com.softdesigner.sicreddesafiotecnico.exception.*;
import br.com.softdesigner.sicreddesafiotecnico.rabbit.VotoSender;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;

import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.UNABLE_TO_VOTE;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {
    private final VotoRepository votoRepository;
    private final UserClient userClient;
    private final AssociadoService associadoService;
    private final SessaoService sessaoService;
    private final VotoSender votoSender;

    public Mono<VotoDTO> createVoto(CreateVotoDTO createVotoDTO) {
        final String document = createVotoDTO.getCpf();
        final String sessionId = createVotoDTO.getSessao();
        final VotoEnum votoEnum = createVotoDTO.getVoto();

        return findSessaoById(sessionId)
                .flatMap(session -> validateSessaoAndFindUserByDocument(document, votoEnum, session))
                .switchIfEmpty(Mono.error(new InvalidSessionException()));
    }

    private Mono<VotoDTO> validateSessaoAndFindUserByDocument(String document, VotoEnum votoEnum, SessaoDocument sessao) {
        if(sessao.getTime().isBefore(now())) {
            log.info("M=validateSessaoAndFindUserByDocument, message=A sessão já foi encerrada, sessao={}",
                    sessao.getId());
            return Mono.error(new ViolateTimeSessionException());
        }
        return getUserStatusByDocumentClientRest(document)
                .flatMap(userStatus -> verifyUserStatusAndCreateVote(userStatus, sessao, document, votoEnum))
                .doOnError(throwable -> Mono.just(new InvalidDocumentException()))
                .switchIfEmpty(Mono.error(new InvalidDocumentException()));
    }

    private Mono<UserStatusDTO> getUserStatusByDocumentClientRest(String document) {
        log.info("M=getUserStatusByDocumentClientRest, message=Iniciado a pesquisa de documento.");
        return userClient.findUserStatusByCpf(document);
    }

    private Mono<VotoDTO> verifyUserStatusAndCreateVote(UserStatusDTO userStatusDTO,
                                               SessaoDocument session,
                                               String document,
                                               VotoEnum voto) {
        if(UNABLE_TO_VOTE.equals(userStatusDTO.getStatus())) {
            log.info("M=verifyDocumentAndCreateVote, message=O usuário não está apto para votar");
            return Mono.error(new UserUnableToVoteException());
        }
        return findOrSaveAssociadoByDocument(document)
                .flatMap(
                        associado ->
                                findAssociadoAndSessao(session, associado)
                                        .hasElement()
                                        .flatMap(alreadyVote ->
                                            validateIfAssociadoAlreadyVoteAndCreateVote(alreadyVote, session, associado, voto))
                );
    }

    private Mono<VotoDTO> validateIfAssociadoAlreadyVoteAndCreateVote(
            boolean alreadyVote,
            SessaoDocument session,
            AssociadoDocument associado,
            VotoEnum voto)
    {
        if (alreadyVote) {
            log.info("M=validateIfAssociadoAlreadyVoteAndCreateVote, message=Já existe registro de voto do associado.");
            return Mono.error(new UserVoteAlreadyExistException());
        }
        return createNewVoto(session, associado, voto);
    }

    private Mono<VotoDocument> findAssociadoAndSessao(SessaoDocument sesao, AssociadoDocument associado) {
        return votoRepository.findByAssociadoAndSessao(associado, sesao);
    }

    private Mono<AssociadoDocument> findOrSaveAssociadoByDocument(String document) {
        return associadoService.findOrCreateAssociadoByCpf(document);
    }

    private Mono<SessaoDocument> findSessaoById(String sessionId) {
        return sessaoService.findByIdDocument(sessionId);
    }

    private Mono<VotoDTO> createNewVoto(SessaoDocument sessao, AssociadoDocument associadoDocument, VotoEnum votoEnum) {
        log.info("M=createVoto, message=Voto registrado");
        final VotoDocument votoDocument = new VotoDocument(associadoDocument, sessao, votoEnum.getValue());
        return votoRepository.save(votoDocument).map(this::converteAndSendVoto);
    }

    private VotoDTO converteAndSendVoto(VotoDocument votoDocument) {
        var votoDTO = VotoConverver.toDTO(votoDocument);
        votoSender.sendVoto(votoDTO);
        return votoDTO;
    }
}
