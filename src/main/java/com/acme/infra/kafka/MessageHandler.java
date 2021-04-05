package com.acme.infra.kafka;

/**
 * An instance of this interface used by the subscriber for message processing
 */
public interface MessageHandler {

    // Called for each message that is received
    public void handle(String topic, int partition, long offset, String key, String value);
}
