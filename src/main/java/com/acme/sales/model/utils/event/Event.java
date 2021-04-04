package com.acme.sales.model.utils.event;

/**
 * Represents an event
 */
abstract public class Event {
    public final String  id;
    public final long    createdAt;
    public final String  name;
    public final Object  payload;

    /**
     * Create the event instance
     */
    public Event(String name, Object payload) {

        // This should be replaced with a more robust mechanism
        // In a multi threaded - high volume system 2 events may have the same ID !!!
        this.id = "ID:"+System.currentTimeMillis();

        this.name = name;
        this.payload = payload;
        createdAt = System.currentTimeMillis();
    }
}
