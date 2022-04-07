package br.com.softdesigner.sicreddesafiotecnico.controller;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
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
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
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
        PautaDocument pautaDocument = new PautaDocument("123456789","Teste de pauta");
        CreatePautaDTO createPautaDTO = new CreatePautaDTO(pautaDocument.getNome());

        given(pautaRepository.save(pautaDocument)).willReturn(Mono.just(pautaDocument));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createPautaDTO), CreatePautaDTO.class)
                .exchange()
                .expectStatus()
                .isCreated();

        verify(pautaRepository).save(any(PautaDocument.class));
    }

    @Test
    @DisplayName("Should find all pautas")
    public void shouldFindAllPautas() {

    }
}
