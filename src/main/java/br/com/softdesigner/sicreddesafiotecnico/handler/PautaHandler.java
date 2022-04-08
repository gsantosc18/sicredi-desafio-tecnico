package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.service.PautaService;
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
public class PautaHandler {
    private final PautaService pautaService;

    @Autowired
    public PautaHandler(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    public Mono<ServerResponse> createNewPauta(ServerRequest request) {
        log.info("M=createNewPauta, message=Create new pauta init");
        Mono<CreatePautaDTO> createPautaDTO = request.bodyToMono(CreatePautaDTO.class);
        return status(CREATED).contentType(APPLICATION_JSON)
                .body(createPautaDTO.flatMap(pautaService::createPauta),PautaDTO.class);
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("M=getAll, message=Get all pauta init");
        return ok().contentType(APPLICATION_JSON)
                .body(pautaService.getAll(),PautaDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        log.info("M=findById, message=Find pauta by id init");
        final String id = request.pathVariable("id");
        return pautaService.findById(id)
                .flatMap(pautaDTO -> ok().bodyValue(pautaDTO))
                .switchIfEmpty(notFound().build());
    }
}
