package br.com.softdesigner.sicreddesafiotecnico.controller;

import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.service.PautaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/pauta")
public class PautaController {
    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping
    public Flux<PautaDTO> getAll() {
        return pautaService.getAll();
    }

    @PostMapping
    public ResponseEntity createPauta(CreatePautaDTO createPautaDTO) {
        Mono<PautaDTO> createdPauta = pautaService.createPauta(createPautaDTO);
        return ResponseEntity.status(CREATED).body(createdPauta);
    }
}
