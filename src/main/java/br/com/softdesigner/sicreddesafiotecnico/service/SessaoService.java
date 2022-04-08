package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.converter.SessaoConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Autowired
    public SessaoService(SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    public Mono<SessaoDTO> createSessao(CreateSessaoDTO createSessaoDTO) {
        final LocalDateTime time = LocalDateTime.now().plusMinutes(createSessaoDTO.getMinutes());
        final PautaDocument pautaDocument = new PautaDocument(createSessaoDTO.getPauta(),null);
        SessaoDocument sessaoDocument = new SessaoDocument(null, pautaDocument,time);
        return sessaoRepository.save(sessaoDocument).map(SessaoConverter::toDto);
    }

    public Mono<SessaoDTO> findById(String id) {
        return sessaoRepository.findById(id).map(SessaoConverter::toDto);
    }
}
