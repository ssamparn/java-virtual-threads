package com.java.programming.javavirtualthreadstripadvisorapp.web.controller;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.reservation.FlightReservationResponse;
import com.java.programming.javavirtualthreadstripadvisorapp.service.TripPlanService;
import com.java.programming.javavirtualthreadstripadvisorapp.service.TripReservationService;
import com.java.programming.javavirtualthreadstripadvisorapp.web.model.request.TripReservationRequest;
import com.java.programming.javavirtualthreadstripadvisorapp.web.model.response.tripplanning.TripPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripAdvisorController {

    private final TripPlanService tripPlanService;
    private final TripReservationService tripReservationService;

    @GetMapping("/{airportCode}")
    public TripPlan planTrip(@PathVariable String airportCode) {
        log.info("airport code: {}, is Virtual: {}", airportCode, Thread.currentThread().isVirtual());
        return this.tripPlanService.getTripPlan(airportCode);
    }

    @PostMapping("/reserve")
    public FlightReservationResponse reserveFlight(@RequestBody TripReservationRequest request) {
        return tripReservationService.reserveFlights(request);
    }
}
