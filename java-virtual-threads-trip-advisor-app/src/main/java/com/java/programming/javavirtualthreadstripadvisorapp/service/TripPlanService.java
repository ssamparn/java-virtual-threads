package com.java.programming.javavirtualthreadstripadvisorapp.service;

import com.java.programming.javavirtualthreadstripadvisorapp.client.AccommodationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.EventServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.LocalRecommendationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.TransportationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.WeatherServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Accommodation;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Event;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.LocalRecommendations;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Transportation;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Weather;
import com.java.programming.javavirtualthreadstripadvisorapp.web.model.response.tripplanning.TripPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripPlanService {

    private final AccommodationServiceClient accommodationServiceClient;
    private final EventServiceClient eventServiceClient;
    private final LocalRecommendationServiceClient localRecommendationServiceClient;
    private final TransportationServiceClient transportationServiceClient;
    private final WeatherServiceClient weatherServiceClient;
    private final ExecutorService executorService;

    public TripPlan getTripPlan(String airportCode) {

        Future<List<Accommodation>> accommodationsFuture = this.executorService.submit(() -> this.accommodationServiceClient.getAccommodations(airportCode));
        Future<List<Event>> eventsFuture = this.executorService.submit(() -> this.eventServiceClient.getEvents(airportCode));
        Future<LocalRecommendations> localRecommendationsFuture = this.executorService.submit(() -> this.localRecommendationServiceClient.getRecommendations(airportCode));
        Future<Transportation> transportationFuture = this.executorService.submit(() -> this.transportationServiceClient.getTransportation(airportCode));
        Future<Weather> weatherFuture = this.executorService.submit(() -> this.weatherServiceClient.getWeather(airportCode));

        return new TripPlan(airportCode,
                getOrElse(accommodationsFuture, Collections.emptyList()),
                getOrElse(weatherFuture, null),
                getOrElse(eventsFuture, Collections.emptyList()),
                getOrElse(localRecommendationsFuture, null),
                getOrElse(transportationFuture, null));
    }

    private <T> T getOrElse(Future<T> future, T defaultValue) {
        try {
            return future.get();
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        }
        return defaultValue;
    }

}
