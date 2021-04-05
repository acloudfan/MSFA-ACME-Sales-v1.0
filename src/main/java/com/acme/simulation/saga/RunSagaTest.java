package com.acme.simulation.saga;

import com.acme.simulation.saga.booking.BookingSagaRepository;

/**
 * Used for testing the Booking SAGA flow
 * MUST change booking_id for each run - otherwise flow will not be triggered !!!
 */
public class RunSagaTest {

    public static void main(String[] args){
        // Change this as an existing booking id in bookingsaga collection will not lead to any events !!

        int bookingId = 3;
        BookingSagaRepository repo = new BookingSagaRepository();
        repo.addSaga(bookingId);
    }
}
