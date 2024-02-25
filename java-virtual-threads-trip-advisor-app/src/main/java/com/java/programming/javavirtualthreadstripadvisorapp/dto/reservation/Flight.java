package com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation;

import java.time.LocalDate;

// Response object for Flight Search Service
public record Flight(String flightNumber,
                     String airline,
                     int price,
                     LocalDate date,
                     int flightDurationInMinutes) {
}
