package com.acme.sales.model.services;

/**
 * Pattern: Infrastructure Service
 * Sales system depend on external payment gateway service for processing credit card payments
 * This interface defines the payment gateway interface Idea is that it may be switched if there is a need
 */
public interface PaymentGateway {

    /**
     * This is to process the payment with the credit card provided by the customer
     * @return  the reference code -1 if payment declined
     */
    public PaymentGatewayTransaction processPayment(String creditCardNumber, int expiryMonth, int expiryYear, String zipCode);

    /**
     * Customer may cancel the purchase and hence will be refunded the amount paid.
     * @return
     */
    public PaymentGatewayTransaction processRefund(String reference);


    /**
     * Return the details of transaction in JSON format
     */
    public PaymentGatewayTransactionDetails getTransactionDetails(String transactionReference);


}
