package br.com.softdesigner.sicreddesafiotecnico.controller;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreatePautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/pauta")
@RequiredArgsConstructor
public class PautaController {
    private PautaService pautaService;

    @GetMapping
    public Flux<PautaDTO> getAll() {
        return pautaService.getAll();
    }

    @PostMapping
    public ResponseEntity createPauta(CreatePautaDTO createPautaDTO) {
        pautaService.createPauta(createPautaDTO);
        return status(CREATED).build();
    }
}
