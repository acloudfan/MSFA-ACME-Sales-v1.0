package com.acme.sales.model.tests.event.staticbroker;

import com.acme.sales.model.utils.event.EventBus;
import com.acme.sales.model.utils.event.EventHandler;

/**
 * Demonstrates the Static class based Event Bus mechanism
 */
public class MainTest {

    public static void main(String[] args){

        // 1. Create a handler and register it
        EventHandler  handler = new SomeActionHappened();
        EventBus.register(AggregateRoot.SOME_ACTION_EVENT, handler);

        // 2. Register additional handlers (optional)

        // 3. Assume that an aggregate was pulled from the repository
        AggregateRoot  aggregateRoot = new AggregateRoot();

        // 4. Some action taken on the aggregate
        aggregateRoot.someAction();

        // 5. We should expect the execution of the handler.handle(..) function
    }
}
