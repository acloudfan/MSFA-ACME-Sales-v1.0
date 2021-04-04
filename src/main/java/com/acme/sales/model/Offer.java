package com.acme.sales.model;

import java.util.Date;

/**
 * Strategic Pattern: Value Object
 * Persistence: Only the reference need to be preserved
 * Model: Acme Sales
 * Represents a discount offer that is applied to the proposal. Managed by the Product team.
 */

public class Offer {

    // Some kind of reference code - Not an identity
    // E.g., Spring2021, XmasInJuly
    public final String reference;

    // Represents Percentage between 0 & 100
    public final int discount;

    // Description of the discount
    public final String description;

    // Valid Till this date
    public final Date validTill;

    /** Constructor **/
    public Offer(String reference, int discount, String description, Date validTill) {
        this.reference = reference;
        this.discount = discount;
        this.description = description;
        this.validTill = validTill;
    }

}