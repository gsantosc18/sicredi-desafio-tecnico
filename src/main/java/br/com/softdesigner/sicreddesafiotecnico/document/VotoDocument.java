package br.com.softdesigner.sicreddesafiotecnico.document;

import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Getter
@Setter
@AllArgsConstructor
public class VotoDocument {
    @DocumentReference
    private AssociadoDocument associadoDocument;
    @DocumentReference
    private SessaoDocument sessaoDocument;
    private VotoEnum voto;
}
