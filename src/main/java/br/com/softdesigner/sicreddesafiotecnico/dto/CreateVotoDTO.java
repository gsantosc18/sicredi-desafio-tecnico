package br.com.softdesigner.sicreddesafiotecnico.dto;


import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateVotoDTO {
    private String sessao;
    private String cpf;
    private VotoEnum voto;
}
