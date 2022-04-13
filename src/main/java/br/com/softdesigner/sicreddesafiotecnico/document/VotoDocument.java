package br.com.softdesigner.sicreddesafiotecnico.document;

import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
public class VotoDocument {
    private AssociadoDocument associado;
    private SessaoDocument sessao;
    private VotoEnum voto;
}
