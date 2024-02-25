package com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation;

import java.time.LocalDate;
import java.util.UUID;

// Response object for Flight Reservation Service
public record FlightReservationResponse(UUID reservationId,
                                        String departure,
                                        String arrival,
                                        String flightNumber,
                                        LocalDate tripDate,
                                        int price) {
}
