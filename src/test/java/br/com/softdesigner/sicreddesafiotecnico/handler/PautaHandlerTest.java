package br.com.softdesigner.sicreddesafiotecnico.handler;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.converter.PautaConverter;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.router.PautaRouter;
import br.com.softdesigner.sicreddesafiotecnico.service.PautaService;
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

@ContextConfiguration(classes = {PautaRouter.class, PautaHandler.class})
@WebFluxTest
class PautaHandlerTest extends BaseTest {
    public static final String ENDPOINT = "/pauta";
    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private PautaService pautaService;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    @DisplayName("Should create a new pauta with success")
    public void shouldCreateNewPauta() {
        PautaDocument pautaDocument = getPautaDocument();
        given(pautaService.createPauta(any())).willReturn(Mono.just(PautaConverter.toDto(pautaDocument)));

        webTestClient.post()
                .uri(ENDPOINT)
                .body(Mono.just(getCreatePautaDTO()), CreatePautaDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(pautaDocument.getId())
                .jsonPath("$.nome").isEqualTo(pautaDocument.getNome());
    }

    @Test
    @DisplayName("Should find pauta by id")
    public void shouldFindPautaById() {
        PautaDocument pautaDocument = getPautaDocument();
        PautaDTO pautaDTO = PautaConverter.toDto(pautaDocument);
        given(pautaService.findById(PAUTA_ID)).willReturn(Mono.just(pautaDTO));

        webTestClient.get()
                .uri(ENDPOINT+"/"+PAUTA_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }

    @Test
    @DisplayName("Should find pauta by id and return empty")
    public void shouldFindPautaByIdAndReturnEmpty() {
        given(pautaService.findById(PAUTA_ID)).willReturn(Mono.empty());

        webTestClient.get()
                .uri(ENDPOINT+"/"+PAUTA_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("Should get all pauta")
    public void shouldGetAllPauta() {
        PautaDocument pautaDocument = getPautaDocument();
        PautaDTO pautaDTO = PautaConverter.toDto(pautaDocument);
        given(pautaService.getAll()).willReturn(Flux.just(pautaDTO,pautaDTO));

        webTestClient.get()
                .uri(ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }
}