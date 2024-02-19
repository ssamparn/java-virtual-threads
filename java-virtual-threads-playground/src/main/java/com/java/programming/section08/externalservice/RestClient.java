package com.java.programming.section08.externalservice;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Slf4j
public class RestClient {

    private static final String PRODUCT_SERVICE_URL = "http://localhost:7070/sec01/product/%d";
    private static final String RATING_SERVICE_URL = "http://localhost:7070/sec01/rating/%d";

    public static String getProduct(int productId) {
        return callExternalService(PRODUCT_SERVICE_URL.formatted(productId));
    }

    public static Integer getRating(int ratingId) {
        return Integer.parseInt(
                callExternalService(RATING_SERVICE_URL.formatted(ratingId))
        );
    }

    private static String callExternalService(String url) {
        log.info("External service url {}", url);
        try (InputStream inputStream = URI.create(url).toURL().openStream()) { // stream should be closed
            return new String(inputStream.readAllBytes()); // responsive size is small
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}