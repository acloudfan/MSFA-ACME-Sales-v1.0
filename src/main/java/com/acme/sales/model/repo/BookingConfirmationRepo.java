package com.acme.sales.model.repo;

import com.acme.sales.model.booking.BookingConfirmation;

import java.util.ArrayList;

/**
 * Tactical Pattern: Repository
 * Model: Acme Sales
 * Represents a Booking Confirmation repository
 */
public interface BookingConfirmationRepo {

    /**
     * Add
     */
    public BookingConfirmation  add(BookingConfirmation bookingConfirmation);

    /**
     * Get
     */
    public BookingConfirmation get(int reference);
    public ArrayList<BookingConfirmation> getByCustomer(int customerReference);

    /**
     *
     * Similar to the above except that it restricts the returned bookings to the recent "number" of bookings
     * e.g., "last 5 bookings"
     */
    public ArrayList<BookingConfirmation> getByCustomer(int customerReference, int number);

    /**
     * Remove
     */
    public boolean remove(int reference);

}
