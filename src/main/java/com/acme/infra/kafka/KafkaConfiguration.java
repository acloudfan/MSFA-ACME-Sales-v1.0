package com.acme.infra.kafka;

/**
 * Setup the Kafka parameters in this class
 */
public interface KafkaConfiguration {
    // CLOUDKARAFKA_BROKERS
    public final static String KAFKA_BROKERS="omnibus-01.srvs.cloudkafka.com:9094,omnibus-02.srvs.cloudkafka.com:9094,omnibus-03.srvs.cloudkafka.com:9094";

    // CLOUDKARAFKA_USERNAME
    public final static String SASL_USERNAME="change this";

    // CLOUDKARAFKA_PASSWORD
    public final static String SASL_PASSWORD="change this";
}
