package com.acme.sales.model.tests.cqrs.v3;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.cqrs.v3.command.events.EventsProcessor;

public class TestEventProcessor {

    // SETUP THE AMQP Connection parameters otherwise there will be an error
    public static final String AMQP_URL ="amqps://timxedvo:5wnWNzGWcajUAtKV12nIClznso46qCeG@orangutan.rmq.cloudamqp.com/timxedvo";

    public static final String AMQP_EXCHANGE ="acme.sales.topic";
    public static final String AMQP_TOPIC = "proposal.update";



    public static void main(String[] args){

        // 1. Create an instance of the PubSubService
        PubSubService pubSubService = new PubSubService(AMQP_URL, AMQP_EXCHANGE, AMQP_TOPIC);

        // 2. Create the Event Processor
        EventsProcessor  processor = new EventsProcessor(pubSubService);

        // 3. Start the events processor thread
        new Thread(processor).start();
    }
}
