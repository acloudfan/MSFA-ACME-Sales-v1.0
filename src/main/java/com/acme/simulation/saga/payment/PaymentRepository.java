package com.acme.simulation.saga.payment;

import com.acme.infra.kafka.ProducerService;
import com.acme.infra.mongodb.MongoDBBase;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import com.acme.simulation.saga.booking.BookingSagaState;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

/**
 * Extends the MongoDB Base class
 * Payment data persisted in a MongoDB collection
 */
public class PaymentRepository extends MongoDBBase implements BookingSagaState, BookingSagaTopic, BookingSagaEvents {

    // Collection to store the payment data
    public final String PAYMENT_COLLECTION="payments";

    // Source of Event - sent as part of the events emitted from this class
    public final static String EVENT_SOURCE_PAYMENT_SERVICE="PAYMENT_SERVICE";

    /**
     * Called after the payment processing.
     * Emits: EVENT_BOOKING_PAYMENT_SUCCESS  in case of successful payment processing
     * Emits: EVENT_BOOKING_PAYMENT_FAILURE  in case of failure in payment processing
     */
    public void addPaymentRecord(String eventGuid, int bookingId, double amount, String ccNumber, boolean success ){

        // 1. Make sure event has not already been processed
        Bson bson =  eq("eventGuid",eventGuid);
        long count = getDocumentCount(PAYMENT_COLLECTION, bson );

        if(count > 0){
            System.out.println("Duplicate event - processing aborted !!!");
            return;
        }

        // 2. Create the json and add to mongo DB
        String paymentData = "{ " +
                "'booking_id': "+bookingId+","+
                "'success' :"+success + ", " +
                "'eventSource': '"+EVENT_SOURCE_PAYMENT_SERVICE+"', " +
                "'amount': "+amount+","+
                "'ccNumber': '"+ccNumber+ "', "+
                "'eventGuid': '"+eventGuid+"'" +
                "}";

        // 3. Insert the document for payment
        executeInsert(PAYMENT_COLLECTION, paymentData);

        // 4. Emit an event
        // Uses a local producer to publish event
        ProducerService producerService = new ProducerService(BOOKING_SAGA_TOPIC);
        String key = bookingId+"";
        JSONObject jsonObject= new JSONObject(paymentData);

        if(success){
            jsonObject.put("paymentConfirmation","XAS4255");
            jsonObject.put("event", EVENT_BOOKING_PAYMENT_SUCCESS);
            producerService.publish(key,jsonObject.toString(4),null);
        } else {
            jsonObject.put("event", EVENT_BOOKING_PAYMENT_FAILURE);
            producerService.publish(key,jsonObject.toString(4),null);
        }
    }

    /**
     * Checks the current state of payment for the booking
     */
    public boolean wasPaymentSuccessful(int bookingId){
        Bson filter = eq("booking_id", bookingId);
        FindIterable iterable = find(PAYMENT_COLLECTION, filter);
        ArrayList<Document> docs = new ArrayList<Document>();
        iterable.into(docs);

        if(docs.size()==0){
            System.out.println("No payment record found for BookingID:"+bookingId+". Taking no action!!");
            return false;
        }

        return docs.get(0).getBoolean("success");
    }

    /**
     * Since this event comes for multiple scenarios we need to ensure that proper action is taken
     */
    public void refundIfChargeWasSuccessful(int bookingId){

        // 1. Check if the payment record with success exist in the payment collection
        boolean wasPaymentSuccess = wasPaymentSuccessful(bookingId);


        if(!wasPaymentSuccess){
            // 2. If the payment record does not exist or it exists but marked as failed then no need to refund
            System.out.println("Taking no action as payment is marked as FAILED!!");
        } else {
            // 3. We need to refund the payment
            //    assume the payment was refunded - so update the status
            Bson filter = Filters.eq("booking_id", bookingId);
            Bson updateOperation = combine(set("refunded", true));
            executeUpdate(PAYMENT_COLLECTION, filter, updateOperation, true);
        }
    }



//    public static void main(String[] args){
//        PaymentRepository paymentRepository=new PaymentRepository();
//        boolean success = true;
//        paymentRepository.addPaymentRecord("sgsgsgsg",1324242,14142.22,"ccnumber", success);
//    }
}
