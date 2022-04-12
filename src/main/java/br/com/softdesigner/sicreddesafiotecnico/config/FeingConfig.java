package br.com.softdesigner.sicreddesafiotecnico.config;

import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {UserClient.class})
public class FeingConfig {
}
