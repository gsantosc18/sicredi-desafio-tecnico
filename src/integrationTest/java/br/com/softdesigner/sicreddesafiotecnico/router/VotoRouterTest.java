package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.handler.VotoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import({VotoHandler.class, VotoRouterTest.class})
public class VotoRouterTest extends BaseTest {
    @Autowired
    private WebTestClient webTestClient;
}
