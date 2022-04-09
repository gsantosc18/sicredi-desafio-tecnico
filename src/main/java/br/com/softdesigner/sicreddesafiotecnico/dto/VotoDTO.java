package br.com.softdesigner.sicreddesafiotecnico.dto;

import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    private AssociadoDTO associado;
    private SessaoDTO sessao;
    private VotoEnum voto;
}
