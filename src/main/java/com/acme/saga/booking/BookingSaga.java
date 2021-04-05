package com.acme.saga.booking;

import com.acme.infra.kafka.KafkaConfiguration;
import com.acme.infra.kafka.MessageHandler;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import org.json.JSONObject;

/**
 * This the class that manages the state of the booking
 */
public class BookingSaga implements BookingSagaState, MessageHandler, BookingSagaEvents, BookingSagaTopic, KafkaConfiguration {

    private BookingSagaRepository bookingSagaRepository = new BookingSagaRepository();

    /**
     * Invoked on message receives on booking topc in Kafka
     */
    @Override
    public void handle(String topic, int partition, long offset, String key, String value) {

        System.out.println("Booking SAGA Receieved: "+value);

        JSONObject jsonObject = new JSONObject(value);
        String event = jsonObject.getString("event");
        if(event.equalsIgnoreCase(EVENT_BOOKING_PAYMENT_SUCCESS)){
            // 1. Change the local state to indicate that payment was successful
            //    Update the state to reflect that the payment was successful
            T1_updateStatusToPaymentConfirmed(jsonObject.getInt("booking_id"), jsonObject.getString("paymentConfirmation"));
        } else if(event.equalsIgnoreCase(EVENT_BOOKING_PAYMENT_FAILURE)) {
            // 2. Change the state to booking failed
            //    Update the state to reflect that the payment failed
            C1_updateStatusToPaymentFailed(jsonObject.getInt("booking_id"));
        } else if(event.equalsIgnoreCase(EVENT_BOOKING_RESERVATION_SUCCESS)){
            // 3. Update the state to reflect that reservation was successfull
            T1_2_updateStatusToReservationConfirmed(jsonObject.getInt("booking_id"), jsonObject.getString("confirmationCode"));
        } else if(event.equalsIgnoreCase(EVENT_BOOKING_RESERVATION_FAILURE)){
            // 4. Change the state to booking failed
            C1_updateStatusToPaymentFailed(jsonObject.getInt("booking_id"));
        }
    }

    /**
     * Update the status of the booking to BOOKING_PAYMENT_CONFIRMED
     */
    private void T1_updateStatusToPaymentConfirmed(int bookingId, String paymentConfirmation){
        bookingSagaRepository.updateStatusToPaymentConfirmed(bookingId, paymentConfirmation);
    }

    /**
     * Update the status of the booking to BOOKING_FAILED
     * Compensating transaction to set the state to failed
     */
    private void C1_updateStatusToPaymentFailed(int bookingId){
        bookingSagaRepository.updateStatusToPaymentFailed(bookingId);
    }

    /**
     * Update the status to Booking confirmed
     */
    private void T1_2_updateStatusToReservationConfirmed(int bookingId, String reservationConfirmation){
        bookingSagaRepository.updateReservationConfirmation(bookingId, reservationConfirmation);
    }

    /**
     * Runs the booking com.acme.saga service as a standalone process
     * Following code is for unit testing
     */
//    public static void main(String[] args){
//
//        // 1. Setup the Group ID
//        String groupId = SASL_USERNAME+"-bookingsaga";
//
//        // 2. Create an instance of the kafka consumer service
//        ConsumerService consumerService = new ConsumerService(BOOKING_SAGA_TOPIC, groupId);
//
//        // 3. Create an instance of the event handler
//        BookingSaga bookingSaga = new BookingSaga();
//
//        // 4. Poll for messages
//        long pollingDuration = 1000;
//        consumerService.subscribe(Duration.ofSeconds(pollingDuration), bookingSaga);
//
//    }
}
