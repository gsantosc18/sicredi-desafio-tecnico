package br.com.softdesigner.sicreddesafiotecnico.router.interfaces;

import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.PautaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface PautaRouterIT {
    @RouterOperations({
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
    RouterFunction<ServerResponse> pautaRoute(PautaHandler pautaHandler);
}
