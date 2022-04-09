package br.com.softdesigner.sicreddesafiotecnico.router.interfaces;

import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.SessaoHandler;
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

public interface SessaoRouterIT {
    @RouterOperations({
            @RouterOperation(
                    path = "/sessao",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = POST,
                    beanClass = SessaoHandler.class,
                    beanMethod = "createSessao",
                    operation = @Operation(
                            operationId = "createSessao",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Sessão criada com sucesso",
                                            content = @Content(schema = @Schema(
                                                    implementation = SessaoDTO.class
                                            ))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/sessao/{id}",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = GET,
                    beanClass = SessaoHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "A pauta foi encontrada",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = SessaoDTO.class
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
    RouterFunction<ServerResponse> sessaoRoute(SessaoHandler sessaoHandler);
}
