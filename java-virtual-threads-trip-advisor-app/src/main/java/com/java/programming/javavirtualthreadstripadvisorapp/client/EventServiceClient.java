package com.java.programming.javavirtualthreadstripadvisorapp.client;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class EventServiceClient {

    private final RestClient restClient;

    public List<Event> getEvents(String airportCode) {
        return this.restClient.get()
                .uri("/{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
