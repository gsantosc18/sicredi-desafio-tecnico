package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import br.com.softdesigner.sicreddesafiotecnico.exception.UserUnableToVoteException;
import br.com.softdesigner.sicreddesafiotecnico.exception.ViolateTimeSessionException;
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

    @InjectMocks
    private VotoService votoService;

    @Test
    @DisplayName("Should throw exception invalid document")
    public void shouldThrowExceptionInvalidDocument() {
        given(userClient.findCpf(CPF)).willReturn(null);
        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(Exception.class);
    }

    @Test
    @DisplayName("Should user unable to vote")
    public void shouldUserUnableToVote() {
        given(userClient.findCpf(CPF)).willReturn(getUserStatusUnableToVote());
        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(UserUnableToVoteException.class);
    }

    @Test
    @DisplayName("Shouldn't vote if invalid session")
    public void shouldntVoteIfInvalidSession() {
        given(userClient.findCpf(CPF)).willReturn(getUserStatusAbleToVote());
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessaoTimeInvalid()));

        StepVerifier.create(votoService.createVoto(createVotoDTO()))
                .expectError(ViolateTimeSessionException.class);
    }

    @Test
    @DisplayName("Should create vote success")
    public void shouldCreateVoteSuccess() {
        given(userClient.findCpf(CPF)).willReturn(getUserStatusAbleToVote());
        given(sessaoService.findByIdDocument(SESSION_ID)).willReturn(Mono.just(getSessao()));
        given(associadoService.findOrCreateAssociadoByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(votoRepository.save(any())).willReturn(Mono.just(getVotoDocument()));

        StepVerifier.create(votoService.createVoto(createVotoDTO()))
            .assertNext(voto -> {
                assertThat(voto).isNotNull();
                assertThat(voto.getVoto()).isEqualTo(SIM);
                assertThat(voto.getSessao().getId()).isEqualTo(SESSION_ID);
                assertThat(voto.getSessao().getTime()).isAfter(now());
                assertThat(voto.getAssociado().getCpf()).isEqualTo(CPF);
                assertThat(voto.getAssociado().getId()).isEqualTo(ASSOCIADO_ID);
            });
    }
}