package com.company.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OwnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Owner getOwnerSample1() {
        return new Owner().id(1L).name("name1").gender("gender1");
    }

    public static Owner getOwnerSample2() {
        return new Owner().id(2L).name("name2").gender("gender2");
    }

    public static Owner getOwnerRandomSampleGenerator() {
        return new Owner().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).gender(UUID.randomUUID().toString());
    }
}
