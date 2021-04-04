package com.acme.infra.rabbitmq;

import com.acme.sales.model.utils.event.messaging.MessageHandler;
import com.acme.sales.model.utils.event.messaging.MessagingService;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Encapsulates the messaging API for Rabbit MQ
 *
 * https://www.rabbitmq.com/api-guide.html
 * https://www.rabbitmq.com/releases/rabbitmq-java-client/v3.2.2/rabbitmq-java-client-javadoc-3.2.2/com/rabbitmq/client/package-summary.html
 */
public class PubSubService implements MessagingService {

    public final String AMQP_URL;
    public final String AMQP_EXCHANGE;
    public final String AMQP_TOPIC;

    private ConnectionFactory   connectionFactory ;
    private Connection          connection = null;
    private Channel             channel = null;

    /**
     *
     * @param props contail all of the MQ specific properties
     */
    public PubSubService(HashMap<String, String> props){
        AMQP_URL = props.get("AMQP_URL");

        // Exchange
        AMQP_EXCHANGE = props.get("AMQP_EXCHANGE");

        // this is the routing key in Rabbit MQ terminology
        AMQP_TOPIC = props.get("AMQP_TOPIC");

    }

    /**
     * This creates the connection to Rabbit MQ
     *
     * As per recommendation, keep the connection and channel open for reuse
     * https://www.rabbitmq.com/api-guide.html#connection-and-channel-lifspan
     */
    @Override
    public void start() throws MessagingException {
        // If the channel is already open then throw an exception
        if (channel != null && channel.isOpen()){
            throw new MessagingException("Connection to MQ already open!!!");
        }
        connectionFactory = new ConnectionFactory();

        try {
            connectionFactory.setUri(AMQP_URL);
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch(Exception e){
            throw new MessagingException("Error creating connection/channel for RabbitMQ!!", e);
        }
    }


    /**
     *
     * @param data   String data to be published
     * @throws MessagingException
     *
     * https://www.rabbitmq.com/api-guide.html#publishing
     * https://www.rabbitmq.com/publishers.html
     */
    @Override
    public void publish(String data) throws MessagingException {
        if(channel == null && !channel.isOpen()){
            throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
        }

        byte[] bytes = null;
        try{
            bytes = data.getBytes("UTF-8");
            channel.basicPublish(AMQP_EXCHANGE, AMQP_TOPIC, null, bytes);
        } catch(UnsupportedEncodingException uese){
            throw new MessagingException("Data could not be converted to UTF-8!!",uese);
        } catch (IOException ioe){
            throw new MessagingException("Could not publish to RabbitMQ!!", ioe);
        }

    }

    /**
     * Blocking function
     * @param handler - for handling the message received
     *
     * https://www.rabbitmq.com/api-guide.html#exchanges-and-queues
     */
    @Override
    public void subscribe(MessageHandler handler) throws MessagingException {
        if(channel == null && !channel.isOpen()){
            throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
        }

        try {
            String queueName = channel.queueDeclare().getQueue();
            subscribe(handler, queueName);
        }catch(IOException ioe){
            throw new MessagingException("Error in queue declaration!!", ioe);
        }
    }

    /**
     *
     * @param handler     object for handling the message
     * @param queueName   common queue that may be consumed with multipe handlers
     * @throws MessagingException
     *
     * https://www.rabbitmq.com/api-guide.html#exchanges-and-queues
     */
    @Override
    public void subscribe(MessageHandler handler, String queueName) throws MessagingException {
        if(channel == null && !channel.isOpen()){
            throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
        }

        try {
            channel.queueBind(queueName, AMQP_EXCHANGE, AMQP_TOPIC);

            // Callback function
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                handler.handleMessage(message);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

        } catch(IOException ioe){
            throw new MessagingException("RabbitMQ subscription error !!!", ioe);
        }
    }

    /**
     * Stops the connection | channel
     * @throws MessagingException
     *
     * https://www.rabbitmq.com/api-guide.html#shutdown
     */
    @Override
    public void stop() throws MessagingException{
        if(channel == null && !channel.isOpen()){
            throw new MessagingException("RabbitMQ channel is not open. Please call start() first !!");
        }

        try {
            channel.close();
            connection.close();
        }catch(IOException ioe){
            throw  new MessagingException("Error in closing connection/channel!!", ioe);
        }catch(TimeoutException toe){
            throw  new MessagingException("Error in closing connection/channel!!", toe);
        }
    }
}
