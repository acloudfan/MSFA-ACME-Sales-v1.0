package com.acme.infra.rabbitmq;

import com.acme.sales.model.utils.event.messaging.MessageHandler;
import com.acme.sales.model.utils.event.messaging.MessagingException;

import java.util.Date;
import java.util.HashMap;

/**
 * Class for testing the PubSub
 *
 * In order to use this test you MUST do the following:
 * 1. Setup your RabbitMQ exchange
 *    Type = topic   Name = acme.sales.topic
 * 2. Create a queue.
 *    Name = email.notifications
 *    This a queue that may be serviced by multiple workers.
 * 3. Create the binding between queue and exchange
 *    Binding Routing Key = acme.sales.bookingconfirmed
 *    All messages to this topic will be routed to the queue
 * 4. Copy paste the AMQP_URL in the code
 *
 * Test Publish:
 * 1. Uncomment the line for publish
 * 2. Comment the line for subscribe
 * 3. Execute the class
 * 4. Check the Queue - there should be a message there
 *
 * Test Subscribe:
 * 1. Comment the line for publish
 * 2. Uncomment the line for subscribe
 * 3. Execute the code
 * 4. On the RabbitMQ UI - go to exchange
 * 5. In publish section
 *    Set the Routing key = acme.sales.bookingconfirmed
 *    Put some text in the message
 *    Hit Publish
 * 6. You should see your subscriber pick up the message
 */
public class PubSubServiceTest {

    // MUST replace this URL to your instance on RabbitMQ
    private static final String AMQP_URL="amqps://lolgkfrt:JrxrfAVFIzKhztFY4NCLTtyRwvEIaFEX@shark.rmq.cloudamqp.com/lolgkfrt";

    private static final String AMQP_EXCHANGE = "acme.sales.topic";
    private static final String AMQP_TOPIC = "acme.sales.bookingconfirmed";


    public static void main(String[] args) throws  Exception{

        //1. Create and instance of the PubSubService
        HashMap<String, String> props = setupProperties();
        PubSubService pubSubService = new PubSubService(props);

        //2. Start the connection
        pubSubService.start();

        String message ="This is a test message sent @ "+new Date();

        //3. Test the publish
//        pubSubService.publish(message);

        //4. Test the subscribe
        subscribeTest(pubSubService);

        System.out.println("Press any key to end the test..");
        System.in.read();

        // Stop the connection
        pubSubService.stop();

    }

    private static void subscribeTest(PubSubService pubSubService){

        // Handler for the message
        MessageHandler messageHandler = (message) -> {
            System.out.println(message);
        };

        try {

            pubSubService.subscribe(messageHandler);

        } catch(MessagingException me){
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
