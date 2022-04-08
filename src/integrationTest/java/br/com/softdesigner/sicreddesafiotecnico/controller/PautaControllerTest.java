package br.com.softdesigner.sicreddesafiotecnico.controller;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import br.com.softdesigner.sicreddesafiotecnico.service.PautaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PautaController.class)
@Import({PautaRepository.class, PautaService.class})
public class PautaControllerTest {
    @MockBean
    private PautaRepository pautaRepository;

    @Autowired
    private WebTestClient webTestClient;

    private final String ENDPOINT = "/pauta";

    @Test
    @DisplayName("Should Create new pauta")
    public void shouldCreateNewPauta() {
        CreatePautaDTO createPautaDTO = new CreatePautaDTO(getPautaDocument().getNome());

        given(pautaRepository.save(any()))
            .willReturn(Mono.just(getPautaDocument()));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createPautaDTO), CreatePautaDTO.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(PautaDTO.class);

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
    @DisplayName("Shouldn't find pauta by id")
    public void shouldFindPautaById() {
        given(pautaRepository.findById(anyString()))
                .willReturn(Mono.empty());

        webTestClient.get()
                .uri(ENDPOINT.concat("/").concat(getPautaDocument().getNome()))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    private PautaDocument getPautaDocument() {
        return new PautaDocument("123456789", "Teste de pauta");
    }
}
