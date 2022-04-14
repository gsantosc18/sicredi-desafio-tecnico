package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
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
import static org.mockito.Mockito.verify;

class PautaServiceTest extends BaseTest {
    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    @DisplayName("Should create new pauta")
    void shouldCreateNewPauta() {
        CreatePautaDTO createPautaDTO = new CreatePautaDTO(PAUTA_NAME);

        given(pautaRepository.save(any(PautaDocument.class)))
                .willReturn(Mono.just(getPautaDocument()));

        pautaService.createPauta(createPautaDTO);

        verify(pautaRepository).save(any(PautaDocument.class));
    }

    @Test
    @DisplayName("Should find all pautas")
    void shouldFindAllPautas() {
        PautaDocument pautaDocument = getPautaDocument();
        Flux<PautaDocument> pautaDocumentFlux = Flux.just(pautaDocument);
        given(pautaRepository.findAll())
                .willReturn(pautaDocumentFlux);

        Flux<PautaDTO> serviceAll = pautaService.getAll();

        StepVerifier.create(serviceAll)
                .expectNextMatches(pauta -> pauta.getNome().equals(pautaDocument.getNome()))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Shouldn't find all pautas")
    void shouldntFindAllPautas() {
        given(pautaRepository.findAll())
                .willReturn(Flux.empty());

        Flux<PautaDTO> foundPauta = pautaService.getAll();

        StepVerifier.create(foundPauta)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should find pauta by id")
    void shouldFindPautaById() {
        given(pautaRepository.findById(PAUTA_ID))
                .willReturn(Mono.just(getPautaDocument()));

        Mono<PautaDTO> foundPauta = pautaService.findById(PAUTA_ID);

        StepVerifier.create(foundPauta)
                .assertNext(pautaDTO -> {
                    assertThat(pautaDTO.getNome()).isEqualTo(getPautaDocument().getNome());
                    assertThat(pautaDTO.getId()).isEqualTo(getPautaDocument().getId());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Shouldn't find pauta by id")
    void shouldntFindPautaById() {
        given(pautaRepository.findById(PAUTA_ID))
                .willReturn(Mono.empty());

        Mono<PautaDTO> foundPauta = pautaService.findById(PAUTA_ID);

        StepVerifier.create(foundPauta)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should find by id and return document")
    public void shouldFindDocumentById() {
        given(pautaRepository.findById(PAUTA_ID)).willReturn(Mono.just(getPautaDocument()));

        StepVerifier.create(pautaService.findByIdDocument(PAUTA_ID))
                .assertNext(pautaDocument -> {
                    assertThat(pautaDocument).isInstanceOf(PautaDocument.class);
                });
    }
}