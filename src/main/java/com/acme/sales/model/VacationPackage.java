package com.acme.sales.model;

import com.acme.sales.model.booking.Reservation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Strategic Pattern: Entity
 * Persistence: Stored in a DB with a unique Identity
 * Model: Acme Sales
 * Represents a Vacation Package
 */
public class VacationPackage {

    // Name for the product - pseudo identity
    // Sales agent know the product by this Identity i.e., meaningful in business domain
    private String name;

    // Friendly description
    private String description;

    // Number of nights
    private int   numberOfNights;

    // Vacation packages have  a type
    public  enum vacationPackageType {RESORT, CRUISE, HOTEL_AIR, HOTEL_AIR_CAR}

    // Type of the vacation package
    private vacationPackageType packageType;

    // Suggested retail price for the product - this is set by product management team
    // This is a Per Person price
    private double retailPrice;

    // Date till this product can be purchased
    private Date   validTill;

    // Sometime the product needs to be marked unavailable
    private boolean active;

    // Indicates if the product is sold out
    private boolean soldOut;

    // This is the destination city
    // In case of cruise it is the port of origin
    private String destination;

    // These reservations are part of the package
    private ArrayList<Reservation> reservationHolders;

    public VacationPackage(String name, String description, int numberOfNights, vacationPackageType packageType, double retailPrice,
                           Date validTill, boolean active, boolean soldOut, String destination, ArrayList<Reservation> reservationHolders) {
        this.name = name;
        this.description = description;
        this.numberOfNights = numberOfNights;
        this.packageType = packageType;
        this.retailPrice = retailPrice;
        this.validTill = validTill;
        this.active = active;
        this.soldOut = soldOut;
        this.destination = destination;
        this.reservationHolders = reservationHolders;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public String getDestination() {
        return destination;
    }

    /**
     * Generate the reservations needed for the booking confirmation
     */
    public ArrayList<Reservation>  generateReservationholders() {
        ArrayList<Reservation>  generated = new ArrayList<Reservation>();
        // Iterate over the holders and create the clones
        for(Reservation r : reservationHolders){
            generated.add(r.createClone());
        }
        return generated;
    }

    public Object clone(){
        return this.generateReservationholders();
    }

    public String toString(){
        String str = "Name="+this.name;
        str += " Number nights="+this.numberOfNights+" Dest="+this.destination;
        str += " retailPrice="+this.retailPrice;
        str += "[";
        for(Reservation r : reservationHolders){
            str += "\t" + r ;
        }
        str += "]";

        return str;
    }
}
