package br.com.softdesigner.sicreddesafiotecnico.router;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import br.com.softdesigner.sicreddesafiotecnico.handler.VotoHandler;
import br.com.softdesigner.sicreddesafiotecnico.repository.AssociadoRepository;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.ABLE_TO_VOTE;
import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.UNABLE_TO_VOTE;
import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.SIM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Import({VotoHandler.class, VotoRouter.class})
public class VotoRouterTest extends BaseTest {
    public static final String ENDPOINT = "/voto";

    @MockBean
    private UserClient userClient;

    @MockBean
    private SessaoRepository sessaoRepository;

    @MockBean
    private AssociadoRepository associadoRepository;

    @MockBean
    private VotoRepository votoRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should throw exception if invalid document")
    public void shouldThrowExceptionIfInvalidDocument() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);

        given(userClient.findCpf(CPF)).willReturn(Mono.empty());

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO),CreateVotoDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$").isNotEmpty();

        verify(sessaoRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("Should throw exception if unable user to vote")
    public void shouldThrowExceptionIfUnableUserToVote() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(UNABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO),CreateVotoDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$").isNotEmpty();
    }

    @Test
    @DisplayName("Should throw exception if end session")
    public void shouldThrowExceptionIfEndSession() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(ABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));
        given(sessaoRepository.findById(anyString())).willReturn(Mono.just(getSessaoDocumentInvalid()));
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO),CreateVotoDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$").isNotEmpty();
    }

    @Test
    @DisplayName("Should throw exception if invalid session")
    public void shouldThrowExceptionIfInvalidSession() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(ABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));
        given(sessaoRepository.findById(anyString())).willReturn(Mono.empty());
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO),CreateVotoDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$").isNotEmpty();
    }

    @Test
    @DisplayName("Should to accept vote")
    public void shouldAcceptVote() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(ABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));
        given(sessaoRepository.findById(anyString())).willReturn(Mono.just(getSessaoDocument(15L)));
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.save(any())).willReturn(Mono.just(getVotoDocument()));
        given(votoRepository.findByAssociadoAndSessao(any(),any())).willReturn(Mono.empty());

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO), CreateVotoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.voto").isEqualTo(SIM.toString());
    }

    @Test
    @DisplayName("Should create associado and accept vote")
    public void shouldCreateAssociadoAndAcceptVote() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(ABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));
        given(sessaoRepository.findById(anyString())).willReturn(Mono.just(getSessaoDocument(15L)));
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.empty());
        given(associadoRepository.save(any())).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.save(any())).willReturn(Mono.just(getVotoDocument()));
        given(votoRepository.findByAssociadoAndSessao(any(),any())).willReturn(Mono.empty());

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO), CreateVotoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.voto").isEqualTo(SIM.toString());
    }

    @Test
    @DisplayName("Should throw exception if associado voted")
    public void shouldThrowExceptionIfAssociadoVoted() {
        CreateVotoDTO createVotoDTO = new CreateVotoDTO(SESSION_ID, CPF, SIM);
        UserStatusDTO userStatusDTO = new UserStatusDTO(ABLE_TO_VOTE.getValue());

        given(userClient.findCpf(CPF)).willReturn(Mono.just(userStatusDTO));
        given(sessaoRepository.findById(anyString())).willReturn(Mono.just(getSessaoDocument(15L)));
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.empty());
        given(associadoRepository.save(any())).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.findByAssociadoAndSessao(any(),any())).willReturn(Mono.just(getVotoDocument()));

        webTestClient.post()
                .uri(ENDPOINT)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(createVotoDTO), CreateVotoDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }
}
