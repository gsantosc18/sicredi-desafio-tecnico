package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.SessaoHandler;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Import({SessaoRouter.class, SessaoHandler.class})
public class SessaoRouterTest extends BaseTest {
    public static final String ENDPOINT = "/sessao";
    public static final String FIND_BY_ID = ENDPOINT + "/{id}";
    public static final long MINUTES = 2L;
    @MockBean
    private SessaoRepository sessaoRepository;

    @MockBean
    private PautaRepository pautaRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should create new sessao")
    public void shouldCreateNewSessao(){
        final CreateSessaoDTO createSessaoDTO = new CreateSessaoDTO(PAUTA_ID, MINUTES);

        given(sessaoRepository.save(any())).willReturn(Mono.just(getSessaoDocument(MINUTES)));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createSessaoDTO), CreateSessaoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(getSessaoDocument(MINUTES).getId())
                .jsonPath("$.time").isEqualTo(getTimePlusMinutes(MINUTES).format(ISO_DATE_TIME))
                .jsonPath("$.pauta.id").isEqualTo(getPautaDocument().getId())
                .jsonPath("$.pauta.nome").isEqualTo(getPautaDocument().getNome());
    }

    @Test
    @DisplayName("Should create new sessao with default time")
    public void shouldCreateNewSessaoWithDefaultTime(){
        final Long defaultTime = 1L;
        final LocalDateTime timePlusMinutes = getTimePlusMinutes(defaultTime);
        final CreateSessaoDTO createSessaoDTO = new CreateSessaoDTO(PAUTA_ID, MINUTES);
        final PautaDocument pautaDocument = new PautaDocument(PAUTA_ID, PAUTA_NAME);
        final SessaoDocument sessaoDocument = new SessaoDocument(any(), pautaDocument, timePlusMinutes);

        given(sessaoRepository.save(sessaoDocument)).willReturn(Mono.just(getSessaoDocument(defaultTime)));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createSessaoDTO), CreateSessaoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.time").isEqualTo(timePlusMinutes.format(ISO_DATE_TIME));
    }

    @Test
    @DisplayName("Should find sessao by id")
    public void shouldFindSessaoById() {
        given(sessaoRepository.findById(SESSION_ID)).willReturn(Mono.just(getSessaoDocument(MINUTES)));

        webTestClient.get().uri(FIND_BY_ID,SESSION_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(SESSION_ID)
                .jsonPath("$.time").isEqualTo(getTimePlusMinutes(MINUTES).format(ISO_DATE_TIME));
    }

    @Test
    @DisplayName("Shouldn't find sessao")
    public void shouldntFindSessao() {
        given(sessaoRepository.findById(SESSION_ID)).willReturn(Mono.empty());

        webTestClient.get().uri(FIND_BY_ID,SESSION_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .isEmpty();
    }
}
