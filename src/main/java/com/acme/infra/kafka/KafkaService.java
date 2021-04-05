package com.acme.infra.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Base class for ConsumerService and ProducerService
 * Has the common code needed for the kafka configuration
 */
abstract public class KafkaService implements KafkaConfiguration {

    // Holds the topic
    protected final String topic;

    // Holds the consumer group id
    protected final String groupId;

    public KafkaService(String topic, String groupId){
        this.topic = topic;
        this.groupId = groupId;
    }

    // This will create the default group ID
    // Used by the consumer if no group ID specified
    // Autocommit = true NOT allowed for group id = null, so we are using a default group id
    public KafkaService(String topic){
        this(topic,SASL_USERNAME+"-consumer");
    }



    /**
     * Sets up the properties
     * Refer: https://kafka.apache.org/10/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html
     */
    protected Properties setupProperties(){
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, SASL_USERNAME, SASL_PASSWORD);

        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_BROKERS);
        if(groupId != null) {
            props.put("group.id", groupId);
        }
        // Auto commit = true NOT allowed if groupId = null
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasCfg);

        return props;
    }
}
