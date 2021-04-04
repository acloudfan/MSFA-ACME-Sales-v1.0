package com.acme.sales.model.utils.event;


import java.util.Hashtable;

/**
 * Static class that stores the Event Dispatchers for each Event name
 * Consider doing version# 2 of this by using the strategy -> https://dzimchuk.net/reliable-domain-events/
 */
public class EventBus {
    // Holds an instance of the EventBus for each Event type i.e., name
    private static Hashtable<String, EventDispatcher>  eventDispatchers = new Hashtable();


    /**
     * Raise the event
     * @return - false if no dispatcher found
     */
    public static boolean raise(Event event){
        // 1. Get the dispatcher for the event.name
        EventDispatcher  eventDispatcher = eventDispatchers.get(event.name);

        // 2. Return false if no dispatcher i.e., handler
        if(eventDispatcher == null) return false;

        // 3. Dispatch the event
        return eventDispatcher.dispatch(event);
    }

    /**
     * Register a handler
     * @return false = Does not indicate an error; just that handler is already registered
     */
    public static boolean register(String eventName, EventHandler  handler){

        // 1. Find the dispatcher for the given eventName
        EventDispatcher  eventDispatcher = eventDispatchers.get(eventName);

        // 2. If dispatcher not found then create the dispatcher
        if(eventDispatcher == null){
            eventDispatcher = new EventDispatcher(eventName);
            eventDispatchers.put(eventName,eventDispatcher);
        }
        // 3. Register the handler with the dispatcher
        return eventDispatcher.register(handler);
    }

    /**
     * De register a handler
     * @return false = Does not indicate an error; just that handler was not found
     */
    public static boolean deregister(String eventName,EventHandler  handler){
        EventDispatcher  eventDispatcher = eventDispatchers.get(eventName);
        if(eventDispatcher == null){
            return false;
        }
        return eventDispatcher.deregister(handler);
    }
}





