package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.router.SessaoRouter;
import br.com.softdesigner.sicreddesafiotecnico.service.SessaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest
@ContextConfiguration(classes = {SessaoHandler.class, SessaoRouter.class})
class SessaoHandlerTest extends BaseTest {
    public static final String ENDPOINT = "/sessao";
    @MockBean
    private SessaoService service;

    @Autowired
    private ApplicationContext applicationContext;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    @DisplayName("Should create a new sessao")
    public void shouldCreateNewSession() {
        CreateSessaoDTO createSessaoDTO = new CreateSessaoDTO(PAUTA_ID, 5L);
        given(service.createSessao(any())).willReturn(Mono.just(getSessaoDTO()));

        webTestClient.post()
                .uri(ENDPOINT)
                .body(Mono.just(createSessaoDTO),CreateSessaoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.pauta.id").isEqualTo(getPautaDTO().getId())
                .jsonPath("$.pauta.nome").isEqualTo(getPautaDTO().getNome());
    }

    @Test
    @DisplayName("Should get all sessao")
    public void shouldGetAllSessao() {
        given(service.getAll()).willReturn(Flux.just(getSessaoDTO(),getSessaoDTO()));

        webTestClient.get()
                .uri(ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }

    @Test
    @DisplayName("Should find sessao by id")
    public void shouldFindSessaoById() {
        given(service.findById(SESSION_ID)).willReturn(Mono.just(getSessaoDTO()));

        webTestClient.get()
                .uri(ENDPOINT+"/"+SESSION_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pauta.id").isEqualTo(getPautaDTO().getId())
                .jsonPath("$.pauta.nome").isEqualTo(getPautaDTO().getNome());
    }

    @Test
    @DisplayName("Should find sessao by id and return not found")
    public void shouldFindSessaoByIdAndReturnNotFound() {
        given(service.findById(SESSION_ID)).willReturn(Mono.empty());

        webTestClient.get()
                .uri(ENDPOINT+"/"+SESSION_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}