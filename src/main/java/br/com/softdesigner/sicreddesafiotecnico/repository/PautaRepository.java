package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends ReactiveCrudRepository<PautaDocument, String> {
}
