package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.handler.PautaHandler;
import br.com.softdesigner.sicreddesafiotecnico.router.interfaces.PautaRouterIT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Slf4j
@Configuration
public class PautaRouter implements PautaRouterIT {
    private final String ENDPOINT = "/pauta";
    private final String FIND_BY_ID = ENDPOINT+"/{id}";

    @Bean
    public RouterFunction<ServerResponse> pautaRoute(PautaHandler pautaHandler) {
        log.info("M=pauta, message=Configuration init");
        return RouterFunctions
                .route(GET(ENDPOINT).and(accept(APPLICATION_JSON)), pautaHandler::getAll)
                .andRoute(POST(ENDPOINT).and(accept(APPLICATION_JSON)), pautaHandler::createNewPauta)
                .andRoute(GET(FIND_BY_ID).and(accept(APPLICATION_JSON)), pautaHandler::findById);
    }
}
