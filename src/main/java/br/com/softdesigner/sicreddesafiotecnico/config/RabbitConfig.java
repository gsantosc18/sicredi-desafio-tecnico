package br.com.softdesigner.sicreddesafiotecnico.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbit.queues.voto}")
    private String queueVoto;

    @Value("${rabbit.queues.resultado}")
    private String queueResultado;

    @Value("${rabbit.exchange}")
    private String queueExchange;

    @Value("${rabbit.routingkeys.voto}")
    private String votoRoutingKey;

    @Value("${rabbit.routingkeys.resultado}")
    private String resultadoRoutingKey;

    @Bean
    public Queue queueVoto() {
        return new Queue(queueVoto, false);
    }

    @Bean
    public Queue queueResultado() {
        return new Queue(queueResultado, false);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(queueExchange);
    }

    @Bean
    public Binding bindingVoto(Queue queueVoto, DirectExchange exchange) {
        return BindingBuilder.bind(queueVoto).to(exchange).with(votoRoutingKey);
    }

    @Bean
    public Binding bindingResultado(Queue queueResultado, DirectExchange exchange) {
        return BindingBuilder.bind(queueResultado).to(exchange).with(resultadoRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
