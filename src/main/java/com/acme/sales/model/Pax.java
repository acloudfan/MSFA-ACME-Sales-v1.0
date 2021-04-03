package com.acme.sales.model;

/**
 * Strategic Pattern: Value Object
 * Persistence: Stored as part of the proposal
 * Model: Acme Sales
 * Represents a Pax or Passengers
 */

public class Pax {

    // Name
    public final String fName;
    public final String mName;
    public final String lName;

    // Age
    public final int age;

    /**
     * Constructor
     * @param fName First name of the passenger
     * @param mName Middle name of the passenger
     * @param lName Last name of the passenger
     * @param age   Age of the passenger
     */
    public Pax(String fName, String mName, String lName, int age) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.age = age;
    }

    public String toString(){
        return "Name="+this.fName+" "+this.mName+" "+this.lName+" Age="+this.age;
    }
}