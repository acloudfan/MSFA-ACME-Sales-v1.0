package com.acme.sales.model.utils.event.messaging;

/**
 * Interface for messaging systems
 */
public interface MessagingService {

    /**
     * This will establish the connection with Messaging infrastructure
     */
    public void start() throws MessagingException;

    /**
     * This is for publishing an event
     */
    public void publish(String data) throws MessagingException;

    /**
     * Subscribe
     * Uses a temporary queue
     * Blocks the caller
     */
    public void subscribe(MessageHandler handler) throws MessagingException;

    /**
     * Uses a declared queue
     */
    public void subscribe(MessageHandler handler, String queueName) throws MessagingException;

    /**
     * This will stop the connection
     */
    public void stop() throws MessagingException;
}
