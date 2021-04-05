package com.acme.simulation.saga.payment;

import com.acme.infra.kafka.ConsumerService;
import com.acme.infra.kafka.MessageHandler;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import com.acme.simulation.saga.util.ServiceSimulator;
import org.json.JSONObject;

import java.time.Duration;
import java.util.UUID;

/**
 * Implements the event handler
 * T2 = Transaction for payment processing
 *      Simulates the payment processing. You may control the success factor for the payment processing
 * C2 = Transaction for refund
 */
public class PaymentService implements MessageHandler, BookingSagaTopic, BookingSagaEvents {

    // 1. Instance of repository
    private final PaymentRepository paymentRepository = new PaymentRepository();

    // 2. This controls the % of times SUCCESS will be simulated
    private static final int SIMULATE_SUCCESS_PERCENT = 75;

    /**
     * Handles the events emitted by the SAGA components
     */
    @Override
    public void handle(String topic, int partition, long offset, String key, String value) {
        System.out.println("Receieved: "+value);
        JSONObject jsonObject = new JSONObject(value);
        String event = jsonObject.getString("event");
        if(event.equalsIgnoreCase(EVENT_BOOKING_ADDED)){
            // 1. Process the payment
            T2_processPayment(jsonObject);
        } if(event.equalsIgnoreCase(EVENT_BOOKING_RESERVATION_FAILURE)){
            // 2. This can happen if reservation has failed
            C2_refundIfChargeWasSuccessful(jsonObject);
        }
    }

    /**
     * Processes the Transaction
     * @param jsonObject
     */
    private void T2_processPayment(JSONObject jsonObject){
        /** This simulates a failure or success **/

        int bookingId = jsonObject.getInt("booking_id");
        String paymentGuid = UUID.randomUUID().toString();

        // 1. Simulate the execution of the payment service
        //    This call uses Thread.sleep() with random sleep time
        boolean success = ServiceSimulator.simulate(SIMULATE_SUCCESS_PERCENT);

        // 2. Repository updates the collection
        //    success= true     Emits  EVENT_BOOKING_PAYMENT_SUCCESS
        //             false    Emits  EVENT_BOOKING_PAYMENT_FAILED
        paymentRepository.addPaymentRecord(paymentGuid, bookingId,999.99,"1234567890", success);

    }


    /**
     * This is invoked when SAGA marks the booking as failed
     * @param jsonObject
     */
    private void C2_refundIfChargeWasSuccessful(JSONObject jsonObject){
        paymentRepository.refundIfChargeWasSuccessful(jsonObject.getInt("booking_id"));
    }


    /**
     * Starts the standalone payment service instance
     * @param args
     */
    public static void main(String[] args){

        // 1. Setup the Group ID
        String groupId = SASL_USERNAME+"-payment";

        // 2. Create an instance of the kafka consumer service
        ConsumerService consumerService = new ConsumerService(BOOKING_SAGA_TOPIC, groupId);

        // 3. Create the handler instance
        PaymentService paymentService = new PaymentService();

        // 4. Poll for incoming messages
        long pollingDuration = 1000;
        consumerService.subscribe(Duration.ofSeconds(pollingDuration), paymentService);

    }

}
