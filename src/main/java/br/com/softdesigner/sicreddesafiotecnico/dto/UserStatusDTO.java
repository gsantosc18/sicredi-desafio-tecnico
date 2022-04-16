package br.com.softdesigner.sicreddesafiotecnico.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDTO implements Serializable {
    private String status;
}
