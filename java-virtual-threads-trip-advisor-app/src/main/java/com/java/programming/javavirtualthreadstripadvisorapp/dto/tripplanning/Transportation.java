package com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning;

import java.util.List;

public record Transportation(List<CarRental> carRentals,
                             List<PublicTransportation> publicTransportations) {
}
