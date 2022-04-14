package br.com.softdesigner.sicreddesafiotecnico.config;

import br.com.softdesigner.sicreddesafiotecnico.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.webclient.WebReactiveFeign;

@Configuration
public class FeingConfig {
    @Value("${user.url}")
    private String url;

    @Bean
    public UserClient userClient() {
        return WebReactiveFeign
                .<UserClient>builder()
                .decode404()
                .target(UserClient.class,url);
    }
}
