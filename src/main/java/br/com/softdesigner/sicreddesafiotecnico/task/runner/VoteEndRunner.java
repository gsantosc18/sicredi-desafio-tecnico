package br.com.softdesigner.sicreddesafiotecnico.task.runner;

import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoResultadoDTO;
import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;
import br.com.softdesigner.sicreddesafiotecnico.rabbit.VotoSender;
import br.com.softdesigner.sicreddesafiotecnico.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.NAO;
import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.SIM;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoteEndRunner implements Runnable{
    private String sessaoId;
    private final VotoRepository votoRepository;
    private final VotoSender votoSender;

    @Override
    public void run() {
        log.info("M=run, message=Fim da sessão");

        Flux<VotoDocument> bySessaoId = votoRepository.findBySessaoId(sessaoId);
        int countSim = countVotos(bySessaoId, SIM).intValue();
        int countNao = countVotos(bySessaoId, NAO).intValue();

        sendToQueueResult(countSim, countNao);
        log.info("M=run, message=Contagem dos votos, sim={}, nao={}", countSim, countNao);
    }

    private void sendToQueueResult(int countSim, int countNao) {
        log.info("M=sendToQueueResult, message=Iniciado envio do resultado para fila");
        final VotoResultadoDTO votoResultadoDTO = new VotoResultadoDTO(countSim, countNao);
        votoSender.sendResultado(votoResultadoDTO);
        log.info("M=sendToQueueResult, message=Finalizado envio do resultado para fila");
    }

    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }

    private Long countVotos(Flux<VotoDocument> bySessaoId, VotoEnum voto) {
        return bySessaoId.filter(votoDocument -> voto.equals(votoDocument.getVoto()))
                .count()
                .block();
    }
}
