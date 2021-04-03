package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.booking.BookingConfirmation;
import com.acme.sales.model.repo.BookingConfirmationRepo;

import java.util.ArrayList;

/**
 * Fake repo implementation for testing
 */
public class BookingConfirmationRepoFake implements BookingConfirmationRepo {

    // In memory collection to simulate the database
    private ArrayList<BookingConfirmation> collection = new ArrayList<>();

    @Override
    public BookingConfirmation add(BookingConfirmation bookingConfirmation) {
        collection.add(bookingConfirmation);
        return bookingConfirmation;
    }

    @Override
    public BookingConfirmation get(int reference) {
        // Loop through the collection
        for(BookingConfirmation bookingConfirmation : collection){
            if(bookingConfirmation.getReference() == reference) {
                return   bookingConfirmation;
            }
        }
        return null;
    }

    @Override
    public ArrayList<BookingConfirmation> getByCustomer(int customerReference) {

        // Using -1 as the number will not match and hence all bookings will be returned
        return this.getByCustomer(customerReference, -1);
    }

    @Override
    public ArrayList<BookingConfirmation> getByCustomer(int customerReference, int number) {
        int  added = 0;
        ArrayList<BookingConfirmation> bookings = new ArrayList<BookingConfirmation>();
        // Loop through the collection
        for(BookingConfirmation bookingConfirmation : collection){
            if(bookingConfirmation.getCustomerReference() == customerReference) {
                bookings.add(bookingConfirmation);
                added++;
                if(added == number) return bookings;
            }
        }
        return bookings;
    }

    @Override
    public boolean remove(int reference) {
        // Loop through the collection
        for(BookingConfirmation bookingConfirmation : collection){
            if(bookingConfirmation.getReference() == reference) {
                collection.remove(bookingConfirmation);
                return   true;
            }
        }
        return false;
    }
}
