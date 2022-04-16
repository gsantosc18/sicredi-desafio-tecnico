package br.com.softdesigner.sicreddesafiotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotoResultadoDTO {
    private int countSim;
    private int countNao;
}
