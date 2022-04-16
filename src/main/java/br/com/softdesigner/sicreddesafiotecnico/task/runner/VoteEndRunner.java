package br.com.softdesigner.sicreddesafiotecnico.task.runner;

import br.com.softdesigner.sicreddesafiotecnico.dto.VotoResultadoDTO;
import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import br.com.softdesigner.sicreddesafiotecnico.rabbit.VotoSender;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteEndRunner implements Runnable{
    private String sessaoId;
    private final VotoRepository votoRepository;
    private final VotoSender votoSender;

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

        sendToQueueResult(countSim, countNao);
        log.info("M=run, message=Contagem dos votos, sim={}, nao={}", countSim.get(), countNao.get());
    }

    private void sendToQueueResult(AtomicInteger countSim, AtomicInteger countNao) {
        log.info("M=sendToQueueResult, message=Iniciado envio do resultado para fila");
        final VotoResultadoDTO votoResultadoDTO = new VotoResultadoDTO(countSim.get(), countNao.get());
        votoSender.sendResultado(votoResultadoDTO);
        log.info("M=sendToQueueResult, message=Finalizado envio do resultado para fila");
    }

    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }
}
