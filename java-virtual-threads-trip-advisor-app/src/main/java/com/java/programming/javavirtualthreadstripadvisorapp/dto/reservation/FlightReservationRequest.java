package com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation;

import java.time.LocalDate;

// Payload for Flight Reservation Service
public record FlightReservationRequest(String departure,
                                       String arrival,
                                       String flightNumber,
                                       LocalDate tripDate) {
}
