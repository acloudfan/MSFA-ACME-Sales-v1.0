package com.acme.simulation.saga.reservations;

import com.acme.infra.kafka.ProducerService;
import com.acme.infra.mongodb.MongoDBBase;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import com.acme.simulation.saga.booking.BookingSagaState;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.json.JSONObject;

/**
 * Extends the MongoDB Base class
 */
public class ReservationsRepository extends MongoDBBase implements BookingSagaState, BookingSagaTopic, BookingSagaEvents {

    public final String RESERVATIONS_COLLECTION="reservations";

    public final static String EVENTS_SOURCE_RESERVATIONS_SERVICE="RESERVATIONS_SERVICE";

    ProducerService producerService = new ProducerService(BOOKING_SAGA_TOPIC);

    /**
     * Called ONLY if the reservation was successful in other words failed reservations attempts are not
     * added to the collection.
     * @param eventGuid
     * @param bookingId
     */
    public void addReservationRecord(String eventGuid, int bookingId,
                                     String packageId, String startDate, String endDate,
                                     String confirmationCode, boolean success ){

        // 1. Make sure event has not already been processed
        Bson bson =  Filters.eq("eventGuid",eventGuid);
        long count = getDocumentCount(RESERVATIONS_COLLECTION, bson );

        if(count > 0){
            System.out.println("Duplicate event - reservations processing aborted !!!");
            return;
        }

        // 2. Create the json and add to mongo DB
        if(!success) confirmationCode = "***FAILED***";
        String reservationData = "{ " +
                "'booking_id': "+bookingId+", " +
                "'success' :"+success + "," +
                "'eventSource': '"+EVENTS_SOURCE_RESERVATIONS_SERVICE+"', " +
                "'confirmationCode': '"+confirmationCode+"', " +
                "'package_id' :'"+packageId+"', " +
                "'startDate' :'"+startDate+"', " +
                "'endDate' : '"+endDate+"', " +
                "'eventGuid': '"+eventGuid+"'"+
                "}";

        // 3. Insert the document for payment
        executeInsert(RESERVATIONS_COLLECTION, reservationData);

        // 4. Emit an event
        String key = bookingId+"";
        JSONObject jsonObject= new JSONObject(reservationData);
//        jsonObject.put("confirmationCode",confirmationCode);
        if(success){
            jsonObject.put("event", EVENT_BOOKING_RESERVATION_SUCCESS);
//            jsonObject.put("confirmationCode",confirmationCode);
            producerService.publish(key,jsonObject.toString(),null);
        } else {
            jsonObject.put("event", EVENT_BOOKING_RESERVATION_FAILURE);
//            jsonObject.put("confirmationCode","***FAILED***");
            producerService.publish(key,jsonObject.toString(),null);
        }
    }



    public static void main(String[] args){
        ReservationsRepository reservationRepository=new ReservationsRepository();
//        reservationRepository.addReservationRecord("sgsgsgsg",1324242,"BAHAMAS5NTCRUISE","01/01/2022","01/05/2022","Confirm-12345");
    }
}
