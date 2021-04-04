package com.acme.sales.model.utils.event;

import java.util.ArrayList;



/**
 * Handles the execution of the Handler
 *
 * Each dispatcher will handle only ONE type of event
 */
public class EventDispatcher {

    public final String eventName;

    ArrayList<EventHandler>  handlers = new ArrayList<>();

    public EventDispatcher(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Registers the handler
     */
    public boolean register(EventHandler handler){

        // 1. Make sure that Hanlder is not registered already - otherwise you have duplication issues !!!
        if(handlers.contains(handler)){
            return false;
        }

        // 2. Add the handler to the collection
        return handlers.add(handler);
    }

    public boolean deregister(EventHandler handler){
        return handlers.remove(handler);
    }

    /**
     * Executes the event handler code for all registered handlers
     * @param event
     */
    public boolean dispatch(Event event){
        // 1. Check if handlers exist
        if(handlers.size() == 0) return false;

        // 2. Loop through the handlers and invoke handle function
        for(EventHandler handler : handlers){
            handler.handle(event);
        }

        // 3. Return true to indicate action was scuccessful
        return true;
    }
}
