package com.java.programming.javavirtualthreadstripadvisorapp.dto.tripplanning;

import java.util.List;

// Response object for Local Recommendation Service
public record LocalRecommendations(List<String> restaurants,
                                   List<String> sightseeing) {
}
