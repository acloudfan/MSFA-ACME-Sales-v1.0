package com.acme.sales.model.tests.event.staticbroker;

import com.acme.sales.model.utils.event.Event;
import com.acme.sales.model.utils.event.EventHandler;

/**
 * Test Handler
 */
public class SomeActionHappened implements EventHandler {

    @Override
    public void handle(Event event) {

        System.out.println(this.getClass()+": Handler executed");
    }
}
