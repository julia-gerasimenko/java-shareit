package ru.practicum.shareitgateway.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {
    private static final String API_PREFIX = "/bookings";

    @Bean
    public BookingClient bookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {

        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        BookingClient client = new BookingClient(serverUrl, restTemplate);
        return client;

    }
}
