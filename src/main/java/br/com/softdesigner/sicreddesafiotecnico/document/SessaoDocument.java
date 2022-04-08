package br.com.softdesigner.sicreddesafiotecnico.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@Getter @Setter
public class SessaoDocument {
    @Id
    private String id;
    @DocumentReference
    private PautaDocument pauta;
    private LocalDateTime time;
}
