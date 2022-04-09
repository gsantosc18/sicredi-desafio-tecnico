package br.com.softdesigner.sicreddesafiotecnico.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
public class AssociadoDocument {
    @Id
    private String id;
    private String cpf;
}
