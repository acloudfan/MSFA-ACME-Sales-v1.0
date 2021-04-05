package com.acme.simulation.saga.reservations;

import com.acme.infra.kafka.ConsumerService;
import com.acme.infra.kafka.KafkaConfiguration;
import com.acme.infra.kafka.MessageHandler;
import com.acme.simulation.saga.BookingSagaEvents;
import com.acme.simulation.saga.BookingSagaTopic;
import com.acme.simulation.saga.util.ServiceSimulator;
import org.json.JSONObject;

import java.time.Duration;
import java.util.UUID;

/**
 * Listens for messages on a common topic
 */
public class ReservationsService implements MessageHandler, BookingSagaEvents, KafkaConfiguration, BookingSagaTopic {

    // This controls the % of times SUCCESS will be simulated
    private static final int SIMULATE_SUCCESS_PERCENT = 65;

    // Instance of repository
    private final ReservationsRepository reservationsRepository = new ReservationsRepository();

    @Override
    public void handle(String topic, int partition, long offset, String key, String value) {
        System.out.println("Receieved: "+value);
        JSONObject jsonObject = new JSONObject(value);
        String event = jsonObject.getString("event");
        if(event.equalsIgnoreCase(EVENT_BOOKING_PAYMENT_SUCCESS)){
            T3_processReservation(jsonObject);
        }
    }

    /**
     * Start the reservation process
     */
    private void T3_processReservation(JSONObject jsonObject){
        /** This simulates a failure or success **/

        int bookingId = jsonObject.getInt("booking_id");
        String reservationGuid = UUID.randomUUID().toString();

        // Simulate the execution of the payment service
        boolean success = ServiceSimulator.simulate(SIMULATE_SUCCESS_PERCENT);

        String packageId = "BAHAMAS5NTCRUISE";
        String startDate = "02/01/2022";
        String endDate = "02/06/2022";
        String reservationConfirmation = "PNR24551";

        reservationsRepository.addReservationRecord(reservationGuid, jsonObject.getInt("booking_id"),
                packageId, startDate, endDate,reservationConfirmation, success);

    }



    /**
     * Runs the payment service as a standalone process
     */
    public static void main(String[] args){

        // Set to null if you would like to try out without group ID
        String groupId = SASL_USERNAME+"-reservtions";

        // 1. Create an instance of the consumer service
        ConsumerService consumerService = new ConsumerService(BOOKING_SAGA_TOPIC, groupId);

        ReservationsService reservationsService = new ReservationsService();

        long pollingDuration = 1000;
        consumerService.subscribe(Duration.ofSeconds(pollingDuration), reservationsService);

    }
}
