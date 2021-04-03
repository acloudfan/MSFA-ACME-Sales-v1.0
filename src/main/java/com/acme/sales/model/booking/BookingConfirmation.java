package com.acme.sales.model.booking;


import com.acme.sales.model.Pax;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.payment.PaymentConfirmation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Strategic Pattern: Aggregate
 * Contains all of the information on the vacation pckage booking
 * Model: Acme Sales
 * Represents the sale proposal prepared for the customer
 */
public class BookingConfirmation {

    /**
     * Enumeration for the state machine
     */
    public enum BookingConfirmationState {
        PENDING_PAYMENT,
        PENDING_RESERVATION,
        RESERVATION_IN_PROGRESS,
        CONFIRMED,
        CANCELLATION_IN_PROGRESS,
        CANCELLED,
        UNKNOWN
    }

    /**
     * Manages state of reservation
     */
    private BookingConfirmationState  status = BookingConfirmationState.UNKNOWN;

    /**
     * Has a unique Booking reference
     */
    private int reference;

    /**
     * Reference to the proposal that customer has committed to
     */
    private int proposalReference;

    /**
     * Reference to the customer record
     */
    private int customerReference;

    /**
     * Holds payment confirmation record
     */
    private PaymentConfirmation paymentConfirmation;

    /**
     * Passengers information
     */
    private ArrayList<Pax>  paxs;

    /**
     * The vacation package may have multiple parts such as Hotel, Car, Air tickets ...
     * The objects representing the reservations are added to this list
     */
    private ArrayList<Reservation> reservations;

    public int getReference() {
        return reference;
    }

    public int getCustomerReference() {
        return customerReference;
    }

    /**
     * Instance is created from the proposal
     */
    public BookingConfirmation(Proposal proposal) {
        this.status = BookingConfirmationState.PENDING_PAYMENT;
        // Setup the reference to proposal
        proposalReference = proposal.getReference();

        // Setup the reference to customer
        customerReference = proposal.getCustomerReference();

        // Get the reservation holders
        // Thes cannot be changed anymore as the customer has committed to the providers and dates
        reservations = proposal.generateReservations();
    }

    /**
     * Existing Booking Proposal
     * @param reference
     * @param proposal
     */
    public BookingConfirmation(int reference, Proposal proposal){
        this(proposal);
        this.reference = reference;
    }

    public BookingConfirmation(int reference, int proposalReference, int customerReference, ArrayList<Reservation> reservations){
        // TBD
    }

    /**
     * Sets the  payment confirmation
     * @param paymentReference
     * @param date
     */
    public void setPaymentConfirmationReference(int paymentReference, Date date) {
        if(paymentConfirmation == null){
            paymentConfirmation = new PaymentConfirmation(paymentReference, date);
        } else {
            throw new RuntimeException("Payment Confirmation is already set !!!");
        }
        this.status = BookingConfirmationState.PENDING_RESERVATION;

        // Trigger the process of Provider Reservations
    }

    public void setCancellationReference(int cancellationReference, Date date){
        if(paymentConfirmation == null){
            throw new RuntimeException("Payment Confirmation is not there to cancel !!!");
        }
        paymentConfirmation.setReferenceNumberCancellation(cancellationReference, date);
        this.status = BookingConfirmationState.CANCELLATION_IN_PROGRESS;

        // Trigger the cancellation of ALL Provider Reservations
    }

    public String toString(){
        String str = "Booking Reference="+this.reference+" Proposal reference="+this.proposalReference+" Customer Reference="+this.customerReference;
        str += " Status="+this.status;
        str += "[";
        for(Reservation r : this.reservations){
            str += "\t" + r + "{ Dates= "+r.getStartDate()+" to "+r.getEndDate()+"}" ;
        }
        str += "]";
        return str;
    }
}
