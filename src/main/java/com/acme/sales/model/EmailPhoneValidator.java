package com.acme.sales.model;

/**
 * Strategic Pattern: Value Object
 * Persistence: Its a Utility; Not persisted to the DB
 * Model: Acme Sales
 * Used for the validation of email & phone number
 */

public class EmailPhoneValidator {

    // Contact information
    public final String email;
    public final String phoneNumber;

    public EmailPhoneValidator(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Validate the email address
    public boolean validateEmailAddress() throws Exception{

        // Logic for email address validation e.g., use Regex

        return true;
    }

    // Validate the phone number
    public boolean validatePhoneNumber() throws Exception {

        // Logic for the phone number validation

        return true;
    }

}
