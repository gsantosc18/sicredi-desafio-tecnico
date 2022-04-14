package br.com.softdesigner.sicreddesafiotecnico.task.runner;

import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import br.com.softdesigner.sicreddesafiotecnico.service.VotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteEndRunner implements Runnable{
    private String sessaoId;
    private final VotoRepository votoRepository;

    @Override
    public void run() {
        AtomicInteger countSim = new AtomicInteger();
        AtomicInteger countNao = new AtomicInteger();

        log.info("M=run, message=Fim da sessão");

        votoRepository.findBySessaoId(sessaoId)
        .subscribe(voto -> {
            if(VotoEnum.SIM.equals(voto.getVoto())) {
                countSim.getAndIncrement();
            } else {
                countNao.getAndIncrement();
            }
        });

        log.info("M=run, message=Contagem dos votos, sim={}, nao={}", countSim.get(), countNao.get());
    }

    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }
}
