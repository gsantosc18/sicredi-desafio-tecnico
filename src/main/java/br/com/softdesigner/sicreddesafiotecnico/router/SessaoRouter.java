package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.handler.SessaoHandler;
import br.com.softdesigner.sicreddesafiotecnico.router.interfaces.SessaoRouterIT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class SessaoRouter implements SessaoRouterIT {
    private final String ENDPOINT = "/sessao";
    private final String FIND_BY_ID = ENDPOINT+"/{id}";
    @Bean
    public RouterFunction<ServerResponse> sessaoRoute(SessaoHandler sessaoHandler) {
        return RouterFunctions.
                route(POST(ENDPOINT).and(accept(APPLICATION_JSON)),sessaoHandler::createSessao)
                .andRoute(GET(FIND_BY_ID).and(accept(APPLICATION_JSON)), sessaoHandler::findById)
                .andRoute(GET(ENDPOINT).and(accept(APPLICATION_JSON)),sessaoHandler::getAll);
    }
}
