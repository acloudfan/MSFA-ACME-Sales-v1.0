package com.acme.infra.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.ArrayList;
import java.util.Properties;


/**
 * Please read the documentation here for more information
 * Refer: https://kafka.apache.org/10/javadoc/org/apache/kafka/clients/producer/package-summary.html
 */
public class ProducerService extends KafkaService {

    // Instance of the Kafka producer
    private KafkaProducer<String, String> kafkaProducer;

    // Constructor
    // Just requires the topic to which message is to be published
    public ProducerService(String topic){
        super(topic);
        Properties props = setupProperties();
        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    // Produce a message without a key
    // Calback receives the status of publish asynchronously
    public void publish(String data, Callback callback){
        // https://kafka.apache.org/10/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html
        // ProducerRecord(java.lang.String topic, K key, V value)

        // 1. Create the producer record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, data);

        // 2. Call the send method on producer
        if(callback == null) {
            kafkaProducer.send(producerRecord);
        } else {
            kafkaProducer.send(producerRecord, callback);
        }

        kafkaProducer.flush();
    }

    // Produce a message with a key
    public void publish(String key, String value, Callback callback){
        // https://kafka.apache.org/10/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html
        // ProducerRecord(java.lang.String topic, K key, V value)

        // 1. Create the producer with the key
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

        // 2. Call the send on producer
        if(callback == null) {
            kafkaProducer.send(producerRecord);
        } else {
            kafkaProducer.send(producerRecord, callback);
        }

        kafkaProducer.flush();
    }

    // Produce a message in a transaction
    public void publishTx(ArrayList<String> dataMessages){
        // 1. Begin the transaction
        kafkaProducer.beginTransaction();

        // 2. Send all messages
        for(String data : dataMessages){
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, data);
            kafkaProducer.send(producerRecord);
        }

        // 3. Commit he messages
        kafkaProducer.commitTransaction();

        // 4. Flush the producer
        kafkaProducer.flush();
    }

    // close the producer
    public void close(){
        kafkaProducer.close();
    }

    /**
     * Unit testing
     */
    public static void main(String[] args) throws Exception{

        // 1. Provide topic name
        // For cloudkarafka you must follow the convention for the topic name
        String topic = SASL_USERNAME + "-" + "default";

        // 2. Setup the producer
        ProducerService producer = new ProducerService(topic);

        // 3. Setup in an instance for asynchronous callback
        Callback callback = new Callback() {
            //https://kafka.apache.org/10/javadoc/org/apache/kafka/clients/producer/RecordMetadata.html

            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                System.out.println("Partition="+metadata.partition()+"    Offset="+metadata.offset());
            }
        };

        // 4. No key specified - comment if needed
        // Partition will be random
//         producer.publish("message produced from java : "+new java.util.Date(), callback);

        // 5. Specify the key
        // All message with common key will end up in the same partition
        String key = "customer-789";
        producer.publish(key, "message produced : " + new java.util.Date(), callback);

    }
}
