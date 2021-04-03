package com.acme.sales.model.services;

import com.acme.sales.model.Customer;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.booking.BookingConfirmation;

import java.util.ArrayList;

/**
 * Represents the response for the CustomerProposalService
 */
public class CustomerHistoryServiceResponse {
    public final Customer  customer;
    public final ArrayList<Proposal> proposals;
    public final ArrayList<BookingConfirmation>  bookings;

    public CustomerHistoryServiceResponse(Customer customer, ArrayList<Proposal> proposals, ArrayList<BookingConfirmation>  bookings ) {
        this.customer = customer;
        this.proposals = proposals;
        this.bookings = bookings;
    }
}
