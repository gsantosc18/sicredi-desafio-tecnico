package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssociadoService {
    private final AssociadoRepository associadoRepository;

    public Mono<AssociadoDocument> findOrCreateAssociadoByCpf(String cpf) {
        return associadoRepository
                .findByCpf(cpf)
                .switchIfEmpty(Mono.defer(() -> createAssociado(cpf)));
    }

    public Mono<AssociadoDocument> createAssociado(String document) {
        final String id = UUID.randomUUID().toString();
        final AssociadoDocument associadoDocument = new AssociadoDocument(id, document);
        return associadoRepository.save(associadoDocument);
    }
}
