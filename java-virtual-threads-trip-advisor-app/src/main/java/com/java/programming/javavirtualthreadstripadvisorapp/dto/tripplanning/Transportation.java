package com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning;

import java.util.List;

// Response object for Local Transportation Service
public record Transportation(List<CarRental> carRentals,
                             List<PublicTransportation> publicTransportations) {
}
