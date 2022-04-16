package br.com.softdesigner.sicreddesafiotecnico.router.interfaces;

import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.VotoHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface VotoRouterIT {
    @RouterOperations(
            @RouterOperation(
                    path = "/voto",
                    method = POST,
                    beanClass = VotoHandler.class,
                    beanMethod = "createVoto",
                    produces = {
                            APPLICATION_JSON_VALUE
                    },
                    operation = @Operation(
                            operationId = "createVoto",
                            responses = @ApiResponse(
                                    responseCode = "201",
                                    description = "Voto efetuado com sucesso",
                                    content = @Content(schema = @Schema(
                                            implementation = VotoDTO.class
                                    ))
                            ),
                            requestBody = @RequestBody(
                                    description = "Parâmetros necessários para a votação",
                                    content = @Content(
                                            schema = @Schema(implementation = CreateVotoDTO.class)
                                    )
                            )
                    )
            )
    )
    RouterFunction<ServerResponse> voto(VotoHandler votoHandler);
}
