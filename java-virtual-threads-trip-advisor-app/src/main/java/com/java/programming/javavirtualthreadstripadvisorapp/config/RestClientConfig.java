package com.java.programming.javavirtualthreadstripadvisorapp.config;

import com.java.programming.javavirtualthreadstripadvisorapp.client.AccommodationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.EventServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.FlightReservationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.FlightSearchServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.LocalRecommendationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.TransportationServiceClient;
import com.java.programming.javavirtualthreadstripadvisorapp.client.WeatherServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class RestClientConfig {

    @Value("${spring.threads.virtual.enabled}")
    private boolean isVirtualThreadEnabled;

    @Bean
    public AccommodationServiceClient accommodationServiceClient(@Value("${service.url.accommodations}") String baseUrl) {
        return new AccommodationServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public EventServiceClient eventServiceClient(@Value("${service.url.events}") String baseUrl) {
        return new EventServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public LocalRecommendationServiceClient localRecommendationServiceClient(@Value("${service.url.local-recommendations}") String baseUrl) {
        return new LocalRecommendationServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public TransportationServiceClient transportationServiceClient(@Value("${service.url.transportation}") String baseUrl) {
        return new TransportationServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public WeatherServiceClient weatherServiceClient(@Value("${service.url.weather}") String baseUrl) {
        return new WeatherServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public FlightSearchServiceClient flightSearchServiceClient(@Value("${service.url.flight-search}") String baseUrl) {
        return new FlightSearchServiceClient(buildRestClient(baseUrl));
    }

    @Bean
    public FlightReservationServiceClient flightReservationServiceClient(@Value("${service.url.flight-reservation}") String baseUrl) {
        return new FlightReservationServiceClient(buildRestClient(baseUrl));
    }

    private RestClient buildRestClient(String baseUrl) {
        log.info("base url: {}", baseUrl);
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl(baseUrl);

        if (isVirtualThreadEnabled) {
            restClientBuilder = restClientBuilder.requestFactory(
                new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                    .executor(Executors.newVirtualThreadPerTaskExecutor())
                    .build())); // rest client builder for virtual threads
        }

        return restClientBuilder.build(); // rest client builder for platform threads
    }

}
