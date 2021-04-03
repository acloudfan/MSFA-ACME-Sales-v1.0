package com.acme.sales.model.booking;

import java.util.Date;

/**
 * All provider reservation classes inherit from this class
 */
abstract public  class Reservation {

    // ID of the Provider
    protected final String provider;
    // Contract code set between ACME and provider
    protected final String contractCode;

    // Reference code provided by the Provider for reservation
    protected String reservationReference;

    // Reference code provided by the Provider for cancellation
    protected String cancellationReference;

    // Dates of reservation
    // In case of HOTEL/RESORT/CRUISE - these are the check in & checkout dates
    protected Date  startDate;
    protected Date  endDate;

    // for the status of the reservation
    enum Status {
        UNKNOWN,
        CONFIRMED,
        CANCELLED
    }

    // types of reservation
    enum ReservationTypes {
        HOTEL,
        AIRLINE,
        CAR_RENTAL,
        CRUISE,
        RESORT
    }

    // Setup by the child class by way of constructor
    public final ReservationTypes reservationType;


    public Reservation(ReservationTypes typ, String provider, String contractCode) {

        this.provider = provider;
        reservationType = typ;
        this.contractCode = contractCode;
    }

    /**
     * This is to initiate the reservation process
     * @return
     */
    public abstract boolean reserve();

    /**
     * This is to initiate the cancellation process
     * @return
     */
    public abstract boolean cancel();

    /**
     * Returns the reservation number
     */
    public String getReservationReference(){
        return reservationReference;
    }

    /**
     * Returns the cancellation reference number
     */
    public String getCancellationReference() { return cancellationReference; }

    /**
     * Gets the reservation type
     * @return
     */
    public ReservationTypes getReservationType() {
        return reservationType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Logic for checking the status
     */
    public Status getStatus(){
        if(cancellationReference != null){
            return Status.CANCELLED;
        } else if(reservationReference != null){
            return  Status.CONFIRMED;
        }

        return Status.UNKNOWN;
    }

    /**
     * Setup the Start & End Dates
     * Concete classes MUST setup the business rules and override this class
     */
    public boolean setupDates(Date startDate, Date endDate){
        /** Rules for validation of dates **/
        this.startDate = startDate;
        this.endDate = endDate;
        return true;
    }

    /**
     * Concrete class must implement this method for creating their clone
     * @return
     */
    abstract public Reservation createClone();

    @Override
    public String toString(){
        String str = "reservationType="+this.reservationType+" "+"Provider="+this.provider;
        return str;
    }
}
