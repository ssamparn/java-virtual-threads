package com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning;

import java.time.LocalDate;

// Response object for Event Service
public record Event(String name,
                    String description,
                    LocalDate date) {
}
