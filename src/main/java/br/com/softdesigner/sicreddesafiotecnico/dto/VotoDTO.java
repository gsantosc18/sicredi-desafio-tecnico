package br.com.softdesigner.sicreddesafiotecnico.dto;

import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class VotoDTO implements Serializable {
    private AssociadoDTO associado;
    private SessaoDTO sessao;
    private VotoEnum voto;
}
