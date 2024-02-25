package com.java.programming.javavirtualthreadstripadvisorapp.client;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class WeatherServiceClient {

    private final RestClient restClient;

    public Weather getWeather(String airportCode) {
        return this.restClient.get()
                .uri("/{airportCode}", airportCode)
                .retrieve()
                .body(Weather.class);
    }
}
