package com.acme.sales.model.services;

import com.acme.sales.model.Customer;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.booking.BookingConfirmation;
import com.acme.sales.model.repo.BookingConfirmationRepo;
import com.acme.sales.model.repo.CustomerRepo;
import com.acme.sales.model.repo.ProposalRepo;

import java.util.ArrayList;

/**
 * Pattern: Application Service
 * Consolidates the history for the customer
 * This service will get the Customer data and Proposals data put it in ONE object and return
 * This service may be exposed as a REST/API that can be invoked from a Front End application
 */
public class CustomerHistoryService {

    // Use the Repo to get the customer data
    private CustomerRepo   customerRepo;

    // Use thet Repo to get the Proposals data
    private ProposalRepo   proposalRepo;

    // Use the Repo to get the Bookings data
    private BookingConfirmationRepo bookingsRepo;

    public CustomerHistoryService(CustomerRepo customerRepo, ProposalRepo proposalRepo) {
        this.customerRepo = customerRepo;
        this.proposalRepo = proposalRepo;
    }

    /**
     * Sets up the response object and returns to the caller
     * @param customerReference
     * @return
     */
    public CustomerHistoryServiceResponse getCustomerHistory(int customerReference, int number){
        Customer customer = customerRepo.get(customerReference);
        // If the customer does not exist then return
        if(customer == null) return null;

        ArrayList<Proposal> proposals = proposalRepo.getCustomerProposals(customerReference, number);
        ArrayList<BookingConfirmation> bookingConfirmations = bookingsRepo.getByCustomer(customerReference, number);

        return new CustomerHistoryServiceResponse(customer, proposals, bookingConfirmations);
    }

    /**
     * Returns all history
     */
    public CustomerHistoryServiceResponse getCustomerHistory(int customerReference){
        return getCustomerHistory(customerReference, -1);
    }

    /**
     * Pull all of the information based on customer's Email or Phone number
     */
    public CustomerHistoryServiceResponse getCustomerHistory(String email, String phoneNumber, int number){
        Customer customer = customerRepo.get(email, phoneNumber);
        // If the customer does not exist then return
        if(customer == null) return null;

        // Get the reference number for the customer
        int customerReference = customer.getReference();

        ArrayList<Proposal> proposals = proposalRepo.getCustomerProposals(customerReference, number);
        ArrayList<BookingConfirmation> bookingConfirmations = bookingsRepo.getByCustomer(customerReference, number);

        return new CustomerHistoryServiceResponse(customer, proposals, bookingConfirmations);
    }

    /**
     * Returns all history
     */
    public CustomerHistoryServiceResponse getCustomerHistory(String email, String phoneNumber) {
        return getCustomerHistory(email, phoneNumber, -1);
    }

}
