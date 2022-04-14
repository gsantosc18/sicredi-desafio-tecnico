package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.converter.SessaoConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import br.com.softdesigner.sicreddesafiotecnico.task.SessionVoteTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@Service
@Slf4j
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final SessionVoteTask sessionTask;
    private final PautaService pautaService;

    @Autowired
    public SessaoService(SessaoRepository sessaoRepository, SessionVoteTask sessionTask, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.sessionTask = sessionTask;
        this.pautaService = pautaService;
    }

    public Mono<SessaoDTO> createSessao(CreateSessaoDTO createSessaoDTO) {
        final String id = UUID.randomUUID().toString();
        final LocalDateTime time = LocalDateTime.now(UTC).plusMinutes(createSessaoDTO.getMinutes());

        return pautaService.findByIdDocument(createSessaoDTO.getPauta())
            .flatMap(pauta -> {
                SessaoDocument sessaoDocument = new SessaoDocument(id, pauta,time);

                sessionTask.schedule(sessaoDocument, dateScheduledTask(time));

                return sessaoRepository.save(sessaoDocument).map(SessaoConverter::toDto);
            });
    }

    public Flux<SessaoDTO> getAll() {
        return sessaoRepository.findAll().map(SessaoConverter::toDto);
    }

    private Date dateScheduledTask(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(UTC));
    }

    public Mono<SessaoDTO> findById(String id) {
        return findByIdDocument(id).map(SessaoConverter::toDto);
    }

    public Mono<SessaoDocument> findByIdDocument(String id) {
        return sessaoRepository.findById(id);
    }
}
