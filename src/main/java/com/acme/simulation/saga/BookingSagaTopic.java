package com.acme.simulation.saga;

import com.acme.infra.kafka.KafkaConfiguration;

/**
 * MUST Create the topic used by Booking SAGA services
 */
public interface BookingSagaTopic extends KafkaConfiguration {
    // Assumes that CloudKarfaka is in use so topic name is following its convention
    // Local broker may be used by adjusting this topic name
    public final static String BOOKING_SAGA_TOPIC = SASL_USERNAME+"-bookingsaga";
}
