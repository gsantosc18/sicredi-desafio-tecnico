package br.com.softdesigner.sicreddesafiotecnico.dto;


import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import lombok.Value;

@Value
public class CreateVotoDTO {
    private VotoEnum votoEnum;
    private String cpf;
}
