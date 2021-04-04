package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.booking.BookingConfirmation;
import com.acme.sales.model.repo.BookingConfirmationRepo;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import com.acme.sales.model.utils.event.messaging.MessagingService;

import java.util.ArrayList;

/**
 * Fake repo implementation for testing
 */
public class BookingConfirmationRepoFake implements BookingConfirmationRepo {

    // In memory collection to simulate the database
    private ArrayList<BookingConfirmation> collection = new ArrayList<>();

    // Added for event messaging
    private MessagingService messagingService;

    @Override
    public BookingConfirmation add(BookingConfirmation bookingConfirmation) {

        // Simulate upsert - by removing the booking confirmation if it already exist
        this.remove(bookingConfirmation.getReference());

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

    /**
     * Save the Booking Confirmation and raise the event BookingConfirmed
     * THIS IS NOT A ROBUST IMPLEMENTATION as DB save and event raising is not atomic
     * Robust pattern discussed in the lecture on "Reliable Messaging"
     * @param bookingConfirmationState
     */
    @Override
    public void updateState(BookingConfirmation  bookingConfirmation, BookingConfirmation.BookingConfirmationState bookingConfirmationState) {
        bookingConfirmation.setStatus(bookingConfirmationState);
        this.add(bookingConfirmation);
        // In real implementation - check for the error before publishing an event
        if(messagingService != null && bookingConfirmationState == BookingConfirmation.BookingConfirmationState.CONFIRMED){
            try {
                String data = "{";
                data += "customerReference: "+ bookingConfirmation.getCustomerReference()+",\n";
                data += "bookingReference: " + bookingConfirmation.getReference()+",\n";
                data += "}";

                // Raise the event
                messagingService.publish(data);

                System.out.println("======Raised the Event via Messaging Service - Check MQ !!!=====");
            } catch(MessagingException me){
                // DB Update and event trigger should be atomic
                // NOW you need to decide !! (a) Do we rollback the update (b) Do we save the event for later procesing
                me.printStackTrace();
            }
        }
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


    /**
     * Set the messaging service - otherwise the raising of events will be ignored - OK for testing
     */
    @Override
    public void setupMessagingService(MessagingService messagingService){
        this.messagingService = messagingService;
    }
}
