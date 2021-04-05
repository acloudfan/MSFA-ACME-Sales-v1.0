package com.acme.infra.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

/**
 * Please read the documentation for the KafkaConsumer for more information
 * https://kafka.apache.org/26/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html
 */

public class ConsumerService extends KafkaService {

    // Used for controlling a loop
    private boolean stopFlag = false;


    /**
     * Creates the consumer without a group ID assignment i.e., each consumer will independently read messages
     * @param topic
     */
    public ConsumerService(String topic){
        super(topic);
    }

    /**
     * Constructor
     * @param topic
     * @param groupId  = Takes the group ID
     */
    public ConsumerService(String topic, String groupId){
        super(topic, groupId);
    }


    /**
     * Subscription is a continuous polling for messages.
     * Unlike AMQP, Kafka does not support push
     * @param pollDuration = timesout for polling
     * @param handler = processing logic for the message
     */
    public void subscribe(Duration pollDuration, MessageHandler handler){

        // 1. Create an insance of the KafkaConsumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(setupProperties());

        // 2. Pass the topic in an array
        consumer.subscribe(Arrays.asList(topic));

        // Setup a loop for message polling
        while(!stopFlag){

            // 3. Consumer gets the partition assignment
            Set<TopicPartition> topicPartitionSet = consumer.assignment();
            System.out.println("Partition assigned="+topicPartitionSet);

            // 4. Poll for messages
            ConsumerRecords<String, String> records = consumer.poll(pollDuration);
            for (ConsumerRecord<String, String> record : records) {

                // 5. Delegate message handling to the handler
                handler.handle(record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

    /**
     * Call to this will end the polling loop
     */
    public void stop(){
        stopFlag = true;
    }

    public static void main(String[] args){
        // Change this for interacting with your own topic
        String topic = SASL_USERNAME+ "-" + "default";

        // Set to null if you would like to try out without group ID
        String groupId = SASL_USERNAME+"-acloudfan";

        // 1. Create an instance of the consumer service
        ConsumerService consumerService = new ConsumerService(topic, groupId);

        // 2. Create an instance of the message handler
        //    This handler simply prints out the message details to the console
        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public void handle(String topic, int partition, long offset, String key, String value) {
                System.out.println("Partition="+partition+" Offset="+offset+"  Key="+key+" Value="+value);
            }
        };

        // 3. Subscribe to the topic
        long pollingDuration = 1000;
        consumerService.subscribe(Duration.ofSeconds(pollingDuration), messageHandler);
    }

}
