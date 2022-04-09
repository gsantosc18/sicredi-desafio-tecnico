package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.PautaHandler;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Import({PautaHandler.class, PautaRouter.class})
public class PautaRouterTest extends BaseTest {
    @MockBean
    private PautaRepository pautaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private final String ENDPOINT = "/pauta";

    @Test
    @DisplayName("Should Create new pauta")
    public void shouldCreateNewPauta() {
        CreatePautaDTO createPautaDTO = new CreatePautaDTO(getPautaDocument().getNome());
        Mono<CreatePautaDTO> publisher = Mono.just(createPautaDTO);

        given(pautaRepository.save(any()))
            .willReturn(Mono.just(getPautaDocument()));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(publisher, CreatePautaDTO.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.nome")
                .isEqualTo(getPautaDocument().getNome())
                .jsonPath("$.id")
                .isEqualTo(getPautaDocument().getId());

        verify(pautaRepository).save(any(PautaDocument.class));
    }

    @Test
    @DisplayName("Should find all pautas")
    public void shouldFindAllPautas() {
        given(pautaRepository.findAll())
                .willReturn(Flux.just(getPautaDocument()));

        webTestClient.get()
            .uri(uriBuilder ->
                uriBuilder.path(ENDPOINT)
                    .queryParam("nome",getPautaDocument().getNome())
                    .build())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].id").isEqualTo(getPautaDocument().getId())
            .jsonPath("$[0].nome").isEqualTo(getPautaDocument().getNome());
    }

    @Test
    @DisplayName("Should find pauta by id")
    public void shouldFindPautaById() {
        given(pautaRepository.findById(anyString()))
                .willReturn(Mono.just(getPautaDocument()));

        webTestClient.get()
                .uri(ENDPOINT.concat("/").concat(getPautaDocument().getNome()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(getPautaDocument().getId())
                .jsonPath("$.nome").isEqualTo(getPautaDocument().getNome());
    }

    @Test
    @DisplayName("Shouldn't find pauta by id")
    public void shouldntFindPautaById() {
        given(pautaRepository.findById(anyString()))
                .willReturn(Mono.empty());

        webTestClient.get()
                .uri(ENDPOINT.concat("/").concat(getPautaDocument().getNome()))
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
