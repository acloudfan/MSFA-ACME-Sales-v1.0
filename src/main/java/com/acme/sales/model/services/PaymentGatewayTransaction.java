package com.acme.sales.model.services;

/**
 * Represents the response to the Payment Gateway Service
 */
public class PaymentGatewayTransaction {
    public final String reference;
    public final String result;
    public final String errorDetails;

    public PaymentGatewayTransaction(String reference, String result, String errorDetails) {
        this.reference = reference;
        this.result = result;
        this.errorDetails = errorDetails;

    }
}
