package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.PautaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Slf4j
@Configuration
public class PautaRouter {
    private final String ENDPOINT = "/pauta";
    private final String FIND_BY_ID = ENDPOINT+"/{id}";

    @Bean
    @RouterOperations ({
            @RouterOperation(
                path = "/pauta",
                produces = {
                        MediaType.APPLICATION_JSON_VALUE
                },
                method = POST,
                beanClass = PautaHandler.class,
                beanMethod = "createNewPauta",
                operation = @Operation(
                    operationId = "createNewPauta",
                    responses = @ApiResponse(
                            responseCode = "204",
                            description = "Pauta criada com sucesso",
                            content = @Content(schema = @Schema(
                                    implementation = PautaDTO.class
                            ))
                        )
                )
            ),
            @RouterOperation(
                    path = "/pauta",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = GET,
                    beanClass = PautaHandler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAll",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Encontrada a lista de pautas",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = PautaDTO.class
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/pauta/{id}",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = GET,
                    beanClass = PautaHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "A pauta foi encontrada",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = PautaDTO.class
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "A pauta não foi encontrada"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> pautaRoute(PautaHandler pautaHandler) {
        log.info("M=pauta, message=Configuration init");
        return RouterFunctions
                .route(GET(ENDPOINT).and(accept(APPLICATION_JSON)), pautaHandler::getAll)
                .andRoute(POST(ENDPOINT).and(accept(APPLICATION_JSON)), pautaHandler::createNewPauta)
                .andRoute(GET(FIND_BY_ID).and(accept(APPLICATION_JSON)), pautaHandler::findById);
    }
}
