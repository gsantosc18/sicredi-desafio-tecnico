package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.converter.VotoConverver;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.router.VotoRouter;
import br.com.softdesigner.sicreddesafiotecnico.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {VotoHandler.class, VotoRouter.class})
@WebFluxTest
class VotoHandlerTest extends BaseTest {
    @MockBean
    private VotoService votoService;

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    @DisplayName("Should create new voto")
    public void shouldCreateVoto() {
        VotoDocument votoDocument = getVotoDocument();
        given(votoService.createVoto(createVotoDTO())).willReturn(Mono.just(VotoConverver.toDTO(votoDocument)));

        webTestClient.post()
                .uri("/voto")
                .body(Mono.just(createVotoDTO()), CreateVotoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.associado.cpf").isEqualTo(createVotoDTO().getCpf());
    }
}