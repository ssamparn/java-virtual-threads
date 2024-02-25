package com.java.programming.javavirtualthreadstripadvisorapp.web.model.request;

import java.time.LocalDate;

public record TripReservationRequest(String departure,
                                     String arrival,
                                     LocalDate date) {
}
