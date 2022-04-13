package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.service.SessaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class SessaoHandler {
    private final SessaoService sessaoService;

    @Autowired
    public SessaoHandler(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    public Mono<ServerResponse> createSessao(ServerRequest request) {
        log.info("M=createSessao, message=Create new sessao init");
        Mono<CreateSessaoDTO> createSessaoDTO = request.bodyToMono(CreateSessaoDTO.class);
        return status(CREATED).contentType(APPLICATION_JSON)
                .body(createSessaoDTO.flatMap(sessaoService::createSessao), SessaoDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        log.info("M=findById, message=Find session by id init");
        String id = request.pathVariable("id");
        return sessaoService.findById(id)
                .flatMap(sessaoDTO -> ok().bodyValue(sessaoDTO))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("M=findById, message=Find session by id init");
        return ok().body(sessaoService.getAll(), SessaoDTO.class);
    }
}
