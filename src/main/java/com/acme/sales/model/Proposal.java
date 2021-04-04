package com.acme.sales.model;

import com.acme.sales.model.booking.Reservation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Strategic Pattern: Entity
 * Created when a customer calls ACME to buy package; provides their preferences.
 * 1. Reservation place holder are created using the VacationPackage
 * 2. Dates and other preferences are then set on the Reservation placeholders
 *    e.g., Hotel reservation, Airline reservation etc
 * Customer may request creation of multiple proposals
 * Model: Acme Sales
 * Represents the sale proposal prepared for the customer
 */
public class Proposal {

    // Proposal reference;
    private int reference;

    // Customer reference
    private int customerReference;

    // Proposal friendly name
    private String friendlyProposalName;

    // Creation date
    private Date createdDate;

    // Proposal expiry
    private Date  expiry;

    // This the product for which the proposal is getting created
    private VacationPackage vacationPackage;

    // Count of the passengers
    private int paxCount;

    // Pax Details
    private ArrayList<Pax> passengers;

    // Customer instructions
    // Agents like to put notes on each proposal that other agents may read
    private ArrayList<String> notes;

    // Price after discount
    private double offerPrice;

    // Business Rule ONLY 2 offers can be applied to a proposal
    private Offer[] offersApplied = new Offer[2];

    // Proposal reservation objects
    private final ArrayList<Reservation>  reservations;

    // Status
    public enum  ProposalStatus {
        ACCEPTED,
        REJECTED, /** Customer has rejected the proposal **/
        ON_HOLD,  /** Customer has not made a decision **/
        AWAITING_CONFIRMATION,   /** Customer has paid but providers need to confirm **/
        EXPIRED,
        UNKNOWN
    }

    // Proposal status
    private ProposalStatus  status=ProposalStatus.UNKNOWN;


    /**
     *
     */
    public Proposal(int  reference, int customerReference,  VacationPackage vacationPackage, Date createdDate) {
        this.reference = reference;
        this.customerReference = customerReference;
        this.createdDate = createdDate;
        this.vacationPackage = vacationPackage;
        this.reservations = vacationPackage.generateReservationholders();
    }

    /**
     * By default all proposals expire in 14 days of creation
     * But sales people have authority to extend up to 30 days -
     * Business Rule: It can not go beyond the validity of the package !!
     * @return
     */
    public boolean setupExpiry(int numDays){
        // Business Rule : New expiry should be < product validity date
        return true;
    }

    private String friendlyProposalName(){
        // Business Rule: 3 letter fname + 3 letter of last name + 3 letter of product + Travel-MM + Travel-DD + Travel-YY
        return "";
    }


    /**
     * Reference number for the proposal
     * @return
     */
    public int getReference() {
        return reference;
    }

    /**
     * Return the customer reference number
     */
    public int getCustomerReference() {
        return customerReference;
    }

    /**
     * Get the reservation at certain index
     */
    public Reservation getReservationAtIndex(int index){
        // NO ERROR Check !!!
        return reservations.get(index);
    }

    /**
     * This applies the offer to the Vacation Product
     */
    public double applyOffer(Offer  offer){
        return 0.0;
    }

    /**
     * Setup the start & end date for reservation at index
     * Error check MUST be fixed - this is for test only
     */
    public boolean setupReservationDates(int index, Date startDate, Date  endDate){
        if(index >= reservations.size() ) return false;
        return reservations.get(index).setupDates(startDate,endDate);
    }

    /**
     * Create clone of the reservations for use in Booking confirmation
     * @return
     */
    public ArrayList<Reservation> generateReservations(){
        ArrayList<Reservation>  generated = new ArrayList<Reservation>();
        // Iterate over the holders and create the clones
        for(Reservation r : reservations){
            generated.add(r.createClone());
        }
        return generated;
    }


    public String toString(){
        String str = "Proposal Reference="+this.reference+" CustomerReference="+this.customerReference+" Date="+this.createdDate;
        str += " Vacation Package=["+this.vacationPackage.getName()+"]";

        str += "[";
        for(Reservation r : this.reservations){
            str += "\t" + r + "{ Dates= "+r.getStartDate()+" to "+r.getEndDate()+"}" ;
        }
        str += "]";


        return  str;
    }

}
