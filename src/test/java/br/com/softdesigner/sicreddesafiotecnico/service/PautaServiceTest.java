package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.repository.PautaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    @DisplayName("Should create new pauta")
    void shouldCreateNewPauta() {
        CreatePautaDTO createPautaDTO = new CreatePautaDTO("Pauta teste");
        PautaDocument pautaDocument = getPautaDocument();

        given(pautaRepository.save(any(PautaDocument.class)))
                .willReturn(Mono.just(pautaDocument));

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
        given(pautaRepository.findById(anyString()))
                .willReturn(Mono.just(getPautaDocument()));

        Mono<PautaDTO> foundPauta = pautaService.findById("123456");

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
        given(pautaRepository.findById(anyString()))
                .willReturn(Mono.empty());

        Mono<PautaDTO> foundPauta = pautaService.findById("123456");

        StepVerifier.create(foundPauta)
                .expectNextCount(0)
                .verifyComplete();
    }

    private PautaDocument getPautaDocument() {
        return new PautaDocument("123456789", "Teste");
    }
}