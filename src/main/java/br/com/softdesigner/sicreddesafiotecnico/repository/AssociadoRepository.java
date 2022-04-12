package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AssociadoRepository extends ReactiveCrudRepository<AssociadoDocument, String> {
    Mono<AssociadoDocument> findByCpf(String cpf);
}
