package com.java.programming.javavirtualthreadstripadvisorapp.service;

import com.java.programming.javavirtualthreadstripadvisorapp.client.FlightReservationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.FlightSearchServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.Flight;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationRequest;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationResponse;
import com.java.programming.javavirtualthreadstripadvisorapp.web.model.request.TripReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripReservationService {

    private final FlightSearchServiceClient flightSearchServiceClient;
    private final FlightReservationServiceClient flightReservationServiceClient;

    // we don't have a specific response object for Trip Reservation.
    // So returning the object directly to the controller what we receive from Flight Reservation Service.

    public FlightReservationResponse reserveFlights(TripReservationRequest request) {
        List<Flight> flights = this.flightSearchServiceClient.searchFlights(request.departure(), request.arrival());

        // now we have to know the flight with the best deal and request details for that flight.
        Flight bestDealflight = flights.stream()
                .min(Comparator.comparingInt(Flight::price))
                .orElseThrow(IllegalStateException::new);

        FlightReservationRequest flightReservationRequest = new FlightReservationRequest(request.departure(), request.arrival(), bestDealflight.flightNumber(), request.date());

        return flightReservationServiceClient.reserveFlight(flightReservationRequest);
    }

}
