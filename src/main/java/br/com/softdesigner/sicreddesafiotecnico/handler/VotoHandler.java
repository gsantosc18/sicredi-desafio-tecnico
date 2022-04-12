package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class VotoHandler {
    private final VotoService votoService;

    @Autowired
    public VotoHandler(VotoService votoService) {
        this.votoService = votoService;
    }

    public Mono<ServerResponse> createVoto(ServerRequest request) {
        Mono<CreateVotoDTO> createVotoDTOMono = request.bodyToMono(CreateVotoDTO.class);

        return status(CREATED)
                .body(createVotoDTOMono.flatMap(votoService::createVoto), VotoDTO.class);
    }
}
