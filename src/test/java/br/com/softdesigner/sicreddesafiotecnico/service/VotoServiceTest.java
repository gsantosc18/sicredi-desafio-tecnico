package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import br.com.softdesigner.sicreddesafiotecnico.exception.InvalidDocumentException;
import br.com.softdesigner.sicreddesafiotecnico.exception.UserUnableToVoteException;
import br.com.softdesigner.sicreddesafiotecnico.exception.UserVoteAlreadyExistException;
import br.com.softdesigner.sicreddesafiotecnico.exception.ViolateTimeSessionException;
import br.com.softdesigner.sicreddesafiotecnico.rabbit.VotoSender;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.SIM;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class VotoServiceTest extends BaseTest {
    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private UserClient userClient;

    @Mock
    private VotoSender votoSender;

    @InjectMocks
    private VotoService votoService;

    @Test
    @DisplayName("Should throw exception invalid document")
    public void shouldThrowExceptionInvalidDocument() {
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(userClient.findCpf(CPF)).willReturn(Mono.empty());
        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(InvalidDocumentException.class)
                .verify();
    }

    @Test
    @DisplayName("Should user unable to vote")
    public void shouldUserUnableToVote() {
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(userClient.findCpf(CPF)).willReturn(Mono.just(getUserStatusUnableToVote()));
        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(UserUnableToVoteException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw exception on consume rest api")
    public void shouldThrowExceptionOnConsumeRestApi() {
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(userClient.findCpf(CPF)).willReturn(Mono.error(new InvalidDocumentException()));
        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(InvalidDocumentException.class)
                .verify();
    }

    @Test
    @DisplayName("Shouldn't vote if invalid session")
    public void shouldntVoteIfInvalidSession() {
        given(userClient.findCpf(CPF)).willReturn(Mono.just(getUserStatusAbleToVote()));
        given(associadoService.findOrCreateAssociadoByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessaoTimeInvalid()));

        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(ViolateTimeSessionException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw exception if user already vote")
    public void shouldThrowExceptionIfUserAlreadyVote() {
        given(userClient.findCpf(CPF)).willReturn(Mono.just(getUserStatusAbleToVote()));
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(associadoService.findOrCreateAssociadoByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.save(any())).willReturn(Mono.just(getVotoDocument()));
        given(votoRepository.findByAssociadoAndSessao(any(),any())).willReturn(Mono.just(getVotoDocument()));

        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(UserVoteAlreadyExistException.class)
                .verify();
    }

    @Test
    @DisplayName("Should create vote success")
    public void shouldCreateVoteSuccess() {
        given(userClient.findCpf(CPF)).willReturn(Mono.just(getUserStatusAbleToVote()));
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(associadoService.findOrCreateAssociadoByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.save(any())).willReturn(Mono.just(getVotoDocument()));
        given(votoRepository.findByAssociadoAndSessao(any(),any())).willReturn(Mono.empty());

        StepVerifier.create(votoService.createVoto(createVotoDTO()))
            .assertNext(voto -> {
                assertThat(voto).isNotNull();
                assertThat(voto.getVoto()).isEqualTo(SIM);
                assertThat(voto.getSessao().getId()).isEqualTo(SESSION_ID);
                assertThat(voto.getSessao().getTime()).isAfter(now());
                assertThat(voto.getAssociado().getCpf()).isEqualTo(CPF);
                assertThat(voto.getAssociado().getId()).isEqualTo(ASSOCIADO_ID);
            })
            .verifyComplete();
    }
}