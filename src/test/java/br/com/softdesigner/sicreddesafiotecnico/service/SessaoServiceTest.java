package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateSessaoDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.SessaoRepository;
import br.com.softdesigner.sicreddesafiotecnico.task.SessionVoteTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class SessaoServiceTest extends BaseTest {
    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private PautaService pautaService;
    @Mock
    private SessionVoteTask sessionTask;

    @InjectMocks
    private SessaoService sessaoService;

    @Test
    @DisplayName("Should create sessao with success")
    public void shouldCreateSessaoWithSuccess() {
        given(pautaService.findByIdDocument(anyString())).willReturn(Mono.just(getPautaDocument()));
        given(sessaoRepository.save(any())).willReturn(Mono.just(getSessao()));

        final CreateSessaoDTO createSessaoDTO = new CreateSessaoDTO("Pauta test", 5L);

        StepVerifier.create(sessaoService.createSessao(createSessaoDTO))
            .assertNext(sessao -> {
                assertThat(sessao).isNotNull();
                assertThat(sessao.getPauta().getId()).isEqualTo(getPautaDocument().getId());
                assertThat(sessao.getPauta().getNome()).isEqualTo(getPautaDocument().getNome());
                assertThat(sessao.getTime()).isNotNull();
                assertThat(sessao.getId()).isEqualTo(getSessao().getId());
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Should get all sessao with success")
    public void shouldGetAllSessaoWithSuccess() {
        given(sessaoRepository.findAll()).willReturn(Flux.just(getSessao(),getSessao()));

        StepVerifier.create(sessaoService.getAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should find id with success")
    public void shouldFindIdWithSuccess() {
        given(sessaoRepository.findById(anyString())).willReturn(Mono.just(getSessao()));

        StepVerifier.create(sessaoService.findById(SESSION_ID))
                .assertNext(sessao -> {
                    assertThat(sessao).isNotNull();
                    assertThat(sessao.getPauta().getId()).isEqualTo(getPautaDocument().getId());
                    assertThat(sessao.getPauta().getNome()).isEqualTo(getPautaDocument().getNome());
                    assertThat(sessao.getTime()).isNotNull();
                    assertThat(sessao.getId()).isEqualTo(getSessao().getId());
                })
                .verifyComplete();
    }
}