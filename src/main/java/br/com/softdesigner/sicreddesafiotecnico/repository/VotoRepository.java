package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VotoRepository extends ReactiveCrudRepository<VotoDocument, String> {
    Mono<VotoDocument> findByAssociadoAndSessao(AssociadoDocument associadoDocument, SessaoDocument sessaoDocument);
    @Query(value = "{'sessao.id': ?0 }")
    Flux<VotoDocument> findBySessaoId(String sessaoId);
}
