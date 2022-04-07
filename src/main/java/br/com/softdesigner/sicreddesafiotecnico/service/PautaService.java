package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.converter.PautaConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PautaService {
    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public void createPauta(CreatePautaDTO createPautaDTO) {
        final String id = UUID.randomUUID().toString();
        final PautaDocument pautaDocument = new PautaDocument(id, createPautaDTO.getNome());
        pautaRepository.save(pautaDocument);
    }

    public Flux<PautaDTO> getAll() {
        return pautaRepository.findAll().map(PautaConverter::toDto);
    }

    public Mono<PautaDTO> findById(String id) {
        return pautaRepository.findById(id).map(PautaConverter::toDto);
    }
}
