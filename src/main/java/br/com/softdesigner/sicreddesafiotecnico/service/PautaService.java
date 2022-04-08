package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.converter.PautaConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PautaService {
    private final PautaRepository pautaRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Mono<PautaDTO> createPauta(CreatePautaDTO createPautaDTO) {
        final String id = UUID.randomUUID().toString();
        final PautaDocument pautaDocument = new PautaDocument(id, createPautaDTO.getNome());
        return pautaRepository.save(pautaDocument).map(PautaConverter::toDto);
    }

    public Flux<PautaDTO> getAll() {
        return pautaRepository.findAll().map(PautaConverter::toDto);
    }

    public Mono<PautaDTO> findById(String id) {
        return pautaRepository.findById(id).map(PautaConverter::toDto);
    }
}
