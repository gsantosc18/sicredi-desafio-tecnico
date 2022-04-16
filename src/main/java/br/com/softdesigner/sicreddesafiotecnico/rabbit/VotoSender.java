package br.com.softdesigner.sicreddesafiotecnico.rabbit;

import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoResultadoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoSender {
    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbit.exchange}")
    private String queueExchange;

    @Value("${rabbit.routingkeys.voto}")
    private String votoRoutingKey;

    @Value("${rabbit.routingkeys.resultado}")
    private String resultadoRoutingKey;

    public void sendVoto(VotoDTO votoDTO) {
        log.info("M=sendVoto, message=Iniciado o envio do voto para a fila.");
        rabbitTemplate.convertAndSend(queueExchange, votoRoutingKey, votoDTO);
        log.info("M=sendVoto, message=Voto enviado com sucesso.");
    }

    public void sendResultado(VotoResultadoDTO votoResultadoDTO) {
        log.info("M=sendVoto, message=Iniciado o envio do resultado para a fila.");
        rabbitTemplate.convertAndSend(queueExchange, resultadoRoutingKey, votoResultadoDTO);
        log.info("M=sendVoto, message=Resultado enviado com sucesso.");
    }
}
