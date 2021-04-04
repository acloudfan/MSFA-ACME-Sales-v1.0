package com.acme.sales.model.tests;

import com.acme.sales.model.services.PaymentGateway;
import com.acme.sales.model.services.PaymentGatewayTransaction;
import com.acme.sales.model.services.PaymentGatewayTransactionDetails;

import java.util.Date;

/**
 * Fake implementation of the Infrastructure Service
 */
public class PaymentGatewayProcessor implements PaymentGateway {
    @Override
    public PaymentGatewayTransaction processPayment(String creditCardNumber, int expiryMonth, int expiryYear, String zipCode, double amount) {

        // This is where the code can be put in for interacting with a 3rd party service

        // Transform the response from the external service to an instance of type PaymentGatewayTransaction

        return new PaymentGatewayTransaction("1234567890", "OK", null);

    }

    @Override
    public PaymentGatewayTransaction processRefund(String reference) {

        // This is where the code can be put in for interacting with a 3rd party service

        // Transform the response from the external service to an instance of type PaymentGatewayTransaction

        return new PaymentGatewayTransaction("0987654321", "OK", null);
    }

    @Override
    public PaymentGatewayTransactionDetails getTransactionDetails(String transactionReference) {

        // Details may be retrieved from the external service

        // Transform the service to create the instance of PaymentGatewayTransactionDetails

        return new PaymentGatewayTransactionDetails("1234567890","John",
                                                "Doe",new Date(),"CHARGE", 908.99);
    }
}
