package br.com.softdesigner.sicreddesafiotecnico.repository;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends ReactiveCrudRepository<AssociadoDocument, String> {
}
