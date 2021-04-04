package com.acme.sales.model;


/**
 * Strategic Pattern: Entity
 * Persistence: Stored in DB as a unique object
 * Model: Acme Sales
 * Represents a Customer
 */

public class Customer {

    // Reference number for each customer
    private int reference;

    // Customer identified uniquely by an email address
    private String email;

    // This is an alternate way to identify the customer
    private String phoneNumber;
    private Address  address;

    // Name
    public final String fName;
    public final String mName;
    public final String lName;



    /**
     * Constructor - first time creation of the object would require the contact information to be validated
     */
    public Customer(String fName, String mName, String lName, Address address, String phoneNumber, String email) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;

        this.address = address;

        this.phoneNumber=phoneNumber;
        this.email=email;
    }

    /**
     * Once a customer is successfully created a reference is created
     * @return
     */
    public void setReference(int reference) {
        this.reference = reference;
    }



    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Return the customer reference
     */
    public int getReference() {

        return reference;
    }

    /**
     * Compares the passed Email & phone number - if anymatch then returns true
     * @return
     */
    public boolean isCustomerEqual(String email, String phoneNumber){
        return (this.email.equalsIgnoreCase(email) || this.phoneNumber.equalsIgnoreCase(phoneNumber));
    }

    public String toString(){
        String str = "reference="+this.reference+System.lineSeparator();
        str += "Name="+this.fName+" "+this.mName+" "+this.lName+System.lineSeparator();
        str += "Email="+this.email+" "+"Phone="+phoneNumber+System.lineSeparator();

        return str;
    }
}
