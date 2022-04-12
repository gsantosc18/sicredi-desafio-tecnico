package br.com.softdesigner.sicreddesafiotecnico.service;

import br.com.softdesigner.sicreddesafiotecnico.BaseTest;
import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.repository.AssociadoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class AssociadoServiceTest extends BaseTest {
    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    @Test
    @DisplayName("Should create associado success")
    public void shouldCreateAssociado() {
        given(associadoRepository.save(any())).willReturn(Mono.just(getAssociadoDocument()));

        AssociadoDocument associado = associadoService.createAssociado(CPF).block();

        assertThat(associado).isNotNull();
        assertThat(associado.getId()).isNotEmpty();
        assertThat(associado.getCpf()).isEqualTo(CPF);
    }

    @Test
    @DisplayName("Should find associado without create")
    public void shouldFindAssociadoWithoutCreate() {
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.just(getAssociadoDocument()));
        given(associadoRepository.save(any())).willReturn(Mono.just(getAssociadoDocument()));

        AssociadoDocument associadoFound = associadoService.findOrCreateAssociadoByCpf(CPF).block();

        verify(associadoRepository,never()).save(any());
        assertThat(associadoFound).isNotNull();
        assertThat(associadoFound.getId()).isEqualTo(ASSOCIADO_ID);
        assertThat(associadoFound.getCpf()).isEqualTo(CPF);
    }

    @Test
    @DisplayName("Should find associado and create")
    public void shouldFindAssociadoAndCreate() {
        given(associadoRepository.findByCpf(CPF)).willReturn(Mono.empty());
        given(associadoRepository.save(any())).willReturn(Mono.just(getAssociadoDocument()));

        AssociadoDocument associadoCreated = associadoService.findOrCreateAssociadoByCpf(CPF).block();

        verify(associadoRepository).save(any());
        assertThat(associadoCreated).isNotNull();
        assertThat(associadoCreated.getId()).isEqualTo(ASSOCIADO_ID);
        assertThat(associadoCreated.getCpf()).isEqualTo(CPF);
    }
}