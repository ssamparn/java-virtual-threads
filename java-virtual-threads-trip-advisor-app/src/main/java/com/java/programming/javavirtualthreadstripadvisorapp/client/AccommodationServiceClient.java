package com.java.programming.javavirtualthreadstripadvisorapp.client;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Accommodation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class AccommodationServiceClient {

    private final RestClient restClient;

    public List<Accommodation> getAccommodations(String airportCode) {
        return this.restClient.get()
                .uri("/{airportCode}", airportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
