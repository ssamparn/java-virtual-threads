package com.java.programming.javavirtualthreadstripadvisorapp.client;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.Flight;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class FlightSearchServiceClient {

    private final RestClient restClient;

    public List<Flight> searchFlights(String departureAirportCode, String arrivalAirportCode) {
        return this.restClient
                .get()
                .uri("/{departureAirportCode}/{arrivalAirportCode}", departureAirportCode, arrivalAirportCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

    }
}
