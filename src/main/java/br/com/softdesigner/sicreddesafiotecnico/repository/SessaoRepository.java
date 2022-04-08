package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends ReactiveCrudRepository<SessaoDocument, String> {
}
