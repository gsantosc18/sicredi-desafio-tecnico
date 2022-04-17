package br.com.softdesigner.sicreddesafiotecnico.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@AllArgsConstructor
public class VotoDocument {
    private AssociadoDocument associado;
    private SessaoDocument sessao;
    private String voto;
}
