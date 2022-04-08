package br.com.softdesigner.sicreddesafiotecnico.dto;

import lombok.Value;

@Value
public class CreateSessaoDTO {
    public static final int DEFAULT_MINUTES = 1;

    private String pauta;
    private Long minutes;

    public long getMinutes() {
        if(minutes!=null) {
            return minutes;
        }
        return DEFAULT_MINUTES;
    }
}
