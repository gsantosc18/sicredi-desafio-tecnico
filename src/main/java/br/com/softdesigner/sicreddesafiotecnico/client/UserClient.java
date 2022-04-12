package br.com.softdesigner.sicreddesafiotecnico.client;

import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserClient", url = "${user.url}", decode404 = true)
public interface UserClient{
    @GetMapping("/users/{cpf}")
    UserStatusDTO findCpf(@PathVariable String cpf);
}
