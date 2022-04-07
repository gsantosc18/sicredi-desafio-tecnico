package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PautaRepository extends ReactiveCrudRepository<PautaDocument, String> {
}
