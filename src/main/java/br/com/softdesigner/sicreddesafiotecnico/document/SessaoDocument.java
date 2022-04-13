package br.com.softdesigner.sicreddesafiotecnico.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@Getter @Setter
public class SessaoDocument {
    @Id
    private String id;
    private PautaDocument pauta;
    private LocalDateTime time;
}
