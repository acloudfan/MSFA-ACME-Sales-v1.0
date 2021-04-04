package com.acme.sales.model.tests.event.bookingconfirmed;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.utils.event.messaging.MessageHandler;
import com.acme.sales.model.utils.event.messaging.MessagingException;

import java.util.HashMap;

/**
 * Test class that demonstrates the use of pub-sub for integration events
 *
 * 1. Subscribes to the event "BookingConfirmed"
 * 2. Reads the message and prints it on console
 *
 * To test:
 * 1. Run this class - it will start to wait for messages
 * 2. In the class tests/event/audit/PaymentAuditTest
 *    Setup the messaging parameters in the function setMessageService
 *    Uncomment the setMessageService function in the main function
 * 3. To simulate BookingConfirmation - execute the class tests/event/audit/PaymentAuditTest
 *    Every time you will run the function - subscriber will receive a message
 */
public class BookingConfirmedSubscriber {
    private static final String AMQP_URL="amqps://lolgkfrt:JrxrfAVFIzKhztFY4NCLTtyRwvEIaFEX@shark.rmq.cloudamqp.com/lolgkfrt";

    private static final String AMQP_EXCHANGE = "acme.sales.topic";
    private static final String AMQP_TOPIC = "acme.sales.bookingconfirmed";

    public static void main(String[] args) throws Exception {
        HashMap<String, String> props = setupProperties();
        PubSubService pubSubService = new PubSubService(props);

        // Start the connection
        pubSubService.start();

        // Handler for the message
        MessageHandler messageHandler = (message) -> {
            System.out.println(message);
        };

        try {

            pubSubService.subscribe(messageHandler);

        } catch (
                MessagingException me) {
            me.printStackTrace();
        }
    }


    private static HashMap<String, String>  setupProperties(){
        HashMap<String, String>  props = new HashMap<>();
        props.put("AMQP_URL", AMQP_URL);
        props.put("AMQP_EXCHANGE", AMQP_EXCHANGE);
        props.put("AMQP_TOPIC", AMQP_TOPIC);

        return props;
    }
}
