package com.java.programming.javavirtualthreadstripadvisorapp.web.model.response.tripplanning;

import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Accommodation;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Event;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.LocalRecommendations;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Transportation;
import com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning.Weather;

import java.util.List;

public record TripPlan(String airportCode,
                       List<Accommodation> accommodations,
                       Weather weather,
                       List<Event> events,
                       LocalRecommendations localRecommendations,
                       Transportation transportation) {
}
