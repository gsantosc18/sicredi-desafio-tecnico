package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.handler.VotoHandler;
import br.com.softdesigner.sicreddesafiotecnico.router.interfaces.VotoRouterIT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class VotoRouter implements VotoRouterIT {
    @Bean
    public RouterFunction<ServerResponse> voto(VotoHandler votoHandler) {
        return RouterFunctions.route(POST("/voto").and(accept(APPLICATION_JSON)),votoHandler::createVoto);
    }
}
