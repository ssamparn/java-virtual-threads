package com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning;

import java.time.LocalDate;

public record Event(String name,
                    String description,
                    LocalDate date) {
}
