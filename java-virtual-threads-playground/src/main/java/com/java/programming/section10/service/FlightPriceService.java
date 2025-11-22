package com.java.programming.section10.service;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class FlightPriceService {

    public static String getDeltaAirFare() {
        log.info("Calling Delta");
        int randomInt = ThreadLocalRandom.current().nextInt(100, 2000);
        CommonUtils.sleep("Delta", Duration.ofMillis(randomInt));
        return "Delta-$" + randomInt;
    }

    public static String getFrontierAirFare() {
        log.info("Calling Frontier");
        int randomInt = ThreadLocalRandom.current().nextInt(100, 3000);
        CommonUtils.sleep("Frontier", Duration.ofMillis(randomInt));
        return "Frontier-$" + randomInt;
    }

    public static String getFailedTask() {
        log.info("Calling Failed Task");
        throw new RuntimeException("oops: 503:Service Unavailable");
    }
}
