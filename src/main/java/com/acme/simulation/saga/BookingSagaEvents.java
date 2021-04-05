package com.acme.simulation.saga;


public interface BookingSagaEvents {
    // 1. Added but not confirmed
    public final static String EVENT_BOOKING_ADDED = "EVENT_BOOKING_ADDED";
    public final static String EVENT_BOOKING_FAILED = "EVENT_BOOKING_FAILED";

    // 2. Booking is now confirmed
    public final static String EVENT_BOOKING_CONFIRMED = "EVENT_BOOKING_CONFIRMED";

    // 3. Payment processing was success or failed - Emitted by Payment
    public final static String EVENT_BOOKING_PAYMENT_SUCCESS = "EVENT_BOOKING_PAYMENT_SUCCESS";
    public final static String EVENT_BOOKING_PAYMENT_FAILURE = "EVENT_BOOKING_PAYMENT_FAILURE";

    // 4. Payment received but booking is pending
//    public final static String EVENT_BOOKING_RESERVATION_PENDING = "EVENT_BOOKING_RESERVATION_PENDING";

    // 5. Reservation processing was success or failed - Emitted by Reservations
    public final static String EVENT_BOOKING_RESERVATION_SUCCESS = "EVENT_BOOKING_RESERVATION_SUCCESS";
    public final static String EVENT_BOOKING_RESERVATION_FAILURE = "EVENT_BOOKING_RESERVATION_FAILURE";
}
