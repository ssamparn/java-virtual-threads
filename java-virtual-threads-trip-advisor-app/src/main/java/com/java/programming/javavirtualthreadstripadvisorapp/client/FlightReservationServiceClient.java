package com.java.programming.javavirtualthreadstripadvisorapp.client;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationRequest;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class FlightReservationServiceClient {

    private final RestClient restClient;

    public FlightReservationResponse reserveFlight(FlightReservationRequest request) {
        return this.restClient
                .post()
                .body(request)
                .retrieve()
                .body(FlightReservationResponse.class);

    }
}
