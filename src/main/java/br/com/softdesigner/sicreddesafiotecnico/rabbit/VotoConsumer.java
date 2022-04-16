package br.com.softdesigner.sicreddesafiotecnico.rabbit;

import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoResultadoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VotoConsumer {
    @RabbitListener(queues = "${rabbit.queues.voto}")
    public void receiverVote(@Payload VotoDTO votoDTO) {
        log.info("M=receiverVote, message=Recebido registro de votação, associado={}, voto={}",
                votoDTO.getAssociado().getId(),
                votoDTO.getVoto());
    }

    @RabbitListener(queues = "${rabbit.queues.resultado}")
    public void receiverResult(@Payload VotoResultadoDTO votoResultadoDTO) {
        log.info("M=receiverVote, message=Recebido resultado da votação, sim={}, nao={}",
                votoResultadoDTO.getCountNao(),
                votoResultadoDTO.getCountSim());
    }
}
