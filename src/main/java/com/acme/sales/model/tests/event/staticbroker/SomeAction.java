package com.acme.sales.model.tests.event.staticbroker;

import com.acme.sales.model.utils.event.Event;

/**
 * This has dependency on AggregateRoot but not the other way round
 */
public class SomeAction extends Event {

    public final static String EVENT_NAME=AggregateRoot.SOME_ACTION_EVENT;

    public SomeAction(Object payload) {
        super(EVENT_NAME, payload);
    }

}
