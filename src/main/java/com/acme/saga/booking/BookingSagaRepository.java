package com.acme.saga.booking;

import com.acme.infra.kafka.ProducerService;
import com.acme.infra.mongodb.MongoDBBase;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.UUID;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

/**
 * Manages the state of the booking as a state machine
 * State data for the booking is persisted in a MongoDB collection
 */
public class BookingSagaRepository extends MongoDBBase implements BookingSagaState, BookingSagaTopic, BookingSagaEvents {

    // Name of collection used by com.acme.saga object
    public final String BOOKING_SAGA_COLLECTION="booking_saga";

    // Source of event sent as part of the event
    public final static String SOURCE_OF_EVENT_BOOKING_SAGA = "BOOKING_SAGA";

    // Uses an instance to publish events to Kafka/topic
    ProducerService producerService = new ProducerService(BOOKING_SAGA_TOPIC);



    /**
     * This is the function that starts the SAGA
     * @param bookingId
     */
    public void addSaga(int bookingId){

        // 1. Check if booikingId already exist
        //    Do not add the record as it already exist
        if(isExists(bookingId)){
            // Booking state already exist in collection
            System.out.println("Booking already exist...aborting add!!");
            return;
        }

        // 2. Create a json representation
        String jsonDoc = "{ 'booking_id' :"+bookingId +
                        ", 'current_state' : '" + BOOKING_PAYMENT_PENDING + "'" +
                     "}";

        // 3. Insert the doc in the SAGA collection for state management
        executeInsert(BOOKING_SAGA_COLLECTION, jsonDoc);

        // 4. Publish an event - EVENT_BOOKING_ADDED
        String guid = UUID.randomUUID().toString();
        String key = bookingId+"";
        String data = "{" +
                "'event_guid': '" + guid +"', "  +
                "'event_source': '" + SOURCE_OF_EVENT_BOOKING_SAGA +"', "  +
                "'event': '" + EVENT_BOOKING_ADDED +"', " +
                "'booking_id': " + bookingId +
                "}";
        producerService.publish(key,data,null);
    }

    /**
     * Processing when the event received = EVENT_BOOKING_PAYMENT_SUCCESS
     * Updates the SAGA instance status = BOOKING_RESERVATION_PENDING
     **/
    public boolean updateStatusToPaymentConfirmed(int bookingId, String paymentConfirmation) {
        if(isExists(bookingId) == false) return false;

        // 2. Get the document
        Bson filter = Filters.eq("booking_id", bookingId);
        Bson updateOperation = combine(set("payment_confirmation", paymentConfirmation),
                set("current_state", BOOKING_RESERVATION_PENDING));
        executeUpdate(BOOKING_SAGA_COLLECTION, filter, updateOperation, true);

        // No event published

        return true;
    }


    /**
     * Processing when the event received = EVENT_BOOKING_RESERVATION_SUCCESS
     * Updates the SAGA instance status = BOOKING_CONFIRMED
     * Emits event = EVENT_BOOKING_CONFIRMED
     */
    public boolean updateReservationConfirmation(int bookingId, String reservationConfirmation) {
        if(isExists(bookingId) == false) return false;

        // 2. Get the document
        Bson filter = Filters.eq("booking_id", bookingId);

        Bson updateOperation = combine(set("reservation_confirmation", reservationConfirmation),
                set("current_state", BOOKING_CONFIRMED));


        executeUpdate(BOOKING_SAGA_COLLECTION, filter, updateOperation, true);

        // Emit the event
        // Publish an event that Booking is now Confirmed
        String guid = UUID.randomUUID().toString();
        String key = bookingId+"";
        String data = "{" +
                "'booking_id': " + bookingId + ","+
                "'event': '" + EVENT_BOOKING_CONFIRMED +"', " +
                "'event_source': '" + SOURCE_OF_EVENT_BOOKING_SAGA +"', "+
                "'event_guid': '" + guid +"'" +
                "}";

        producerService.publish(key,data,null);

        return true;
    }

    /**
     * Called when the payment failed
     * State: BOOKING_FAILED
     * Event: EVENT_BOOKING_FAILED
     */
    public boolean updateStatusToPaymentFailed(int bookingId){
        // 2. Get the document
        Bson filter = Filters.eq("booking_id", bookingId);
        Bson updateOperation = set("current_state", BOOKING_FAILED);
        executeUpdate(BOOKING_SAGA_COLLECTION, filter, updateOperation, true);

        // Publish an event that payment has failed
        String guid = UUID.randomUUID().toString();
        String key = bookingId+"";
        String data = "{" +
                "'booking_id': " + bookingId + ", " +
                "'event': '" + EVENT_BOOKING_FAILED +"', " +
                "'event_source': '" + SOURCE_OF_EVENT_BOOKING_SAGA +"', "  +
                "'event_guid': '" + guid +"'" +
                "}";

        producerService.publish(key,data,null);
        return true;
    }

    /**
     * Checks if the specified bookingId already exist in the SAGA collection
     * @param bookingId
     * @return
     */
    public boolean isExists(int bookingId){
        Bson filter = Filters.eq("booking_id", bookingId);
        return (getDocumentCount(BOOKING_SAGA_COLLECTION,filter) > 0);
    }


//    public boolean updateState(int bookingId, String newState){
//        if(isExists(bookingId) == false) return false;
//
//        // 2. Get the document
//        Bson filter = Filters.eq("booking_id", bookingId);
//        Bson updateOperation = set("current_state", newState);
//        executeUpdate(BOOKING_SAGA_COLLECTION, filter, updateOperation, true);
//
//        return true;
//    }

}
