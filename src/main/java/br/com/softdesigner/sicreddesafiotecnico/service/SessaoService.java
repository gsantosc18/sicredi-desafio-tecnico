package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.converter.SessaoConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import br.com.softdesigner.sicreddesafiotecnico.task.SessionVoteTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Service
@Slf4j
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final SessionVoteTask sessionTask;

    @Autowired
    public SessaoService(SessaoRepository sessaoRepository, SessionVoteTask sessionTask) {
        this.sessaoRepository = sessaoRepository;
        this.sessionTask = sessionTask;
    }

    public Mono<SessaoDTO> createSessao(CreateSessaoDTO createSessaoDTO) {
        final LocalDateTime time = LocalDateTime.now(UTC).plusMinutes(createSessaoDTO.getMinutes());
        final PautaDocument pautaDocument = new PautaDocument(createSessaoDTO.getPauta(),null);
        SessaoDocument sessaoDocument = new SessaoDocument(null, pautaDocument,time);

        sessionTask.schedule(sessaoDocument, dateScheduledTask(time));

        return sessaoRepository.save(sessaoDocument).map(SessaoConverter::toDto);
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
