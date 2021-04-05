package com.acme.simulation.saga.booking;


/**
 * Defines the states for the Booking SAGA
 */
public interface BookingSagaState {
    // 1. Initial state
    public final static String BOOKING_PAYMENT_PENDING = "BOOKING_PAYMENT_PENDING";

    // 2. State after successful payment processing
    public final static String BOOKING_RESERVATION_PENDING = "BOOKING_RESERVATION_PENDING";

    // 3. State after BOTH payment and reservations are successful
    public final static String BOOKING_CONFIRMED = "BOOKING_CONFIRMED";

    // 4. State if any of payment or reservation fails
    public final static String BOOKING_FAILED = "BOOKING_FAILED";
}
