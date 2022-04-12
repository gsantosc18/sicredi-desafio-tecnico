package br.com.softdesigner.sicreddesafiotecnico.dto;

import br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum;
import lombok.Value;

@Value
public class UserStatusDTO {
    private UserStatusEnum status;
}
