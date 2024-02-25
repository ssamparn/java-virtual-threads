package com.java.programming.javavirtualthreadstripadvisorapp;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationRequest;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationResponse;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Accommodation;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Weather;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class RestClientTests {

    private static RestClient restClient;

    @BeforeAll
    static void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:7070")
                .build();
    }

    @Test
    void simpleGetRequestTest() {
        Weather weatherResponse = restClient.get()
                .uri("/sec02/weather/{airportCode}", "LAS")
                .retrieve()
                .body(Weather.class);

        log.info("response: {}", weatherResponse);
    }

    @Test
    void simpleGetRequestListResponseTest() {
        List<Accommodation> accommodations = restClient.get()
                .uri("/sec02/accommodations/{airportCode}", "LAS")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.info("response: {}", accommodations);
    }

    @Test
    void simplePostRequestTest() {
        FlightReservationRequest request = new FlightReservationRequest("ATL", "LAS", "UA789", LocalDate.now());

        FlightReservationResponse flightReservationResponse = restClient.post()
                .uri("/sec03/flight/reserve/")
                .body(request)
                .retrieve()
                .body(FlightReservationResponse.class);

        log.info("response: {}", flightReservationResponse);
    }


}
