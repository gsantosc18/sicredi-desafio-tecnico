package br.com.softdesigner.sicreddesafiotecnico.client;

import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

@Headers({ "Accept: application/json" })
public interface UserClient {
    @RequestLine("GET /users/{cpf}")
    Mono<UserStatusDTO> findUserStatusByCpf(@Param String cpf);
}
