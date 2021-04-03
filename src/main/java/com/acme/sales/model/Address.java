package com.acme.sales.model;

/**
 * Strategic Pattern: Value Object
 * Persistence: Stored in DB as part of a Customer object
 * Model: Acme Sales
 * Represents a physical address
 */

public class Address {
    // Address
    public final String addressLine1;
    public final String addressLine2;
    public final String city;
    public final String state;
    public final String zipcode;

    // Flag is set to true if the address is validated/verified
    private boolean contactInformationValidated;

    public Address(String addressLine1, String addressLine2, String city, String state, String zipcode, boolean contactInformationValidated) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.contactInformationValidated = contactInformationValidated;
    }


}
