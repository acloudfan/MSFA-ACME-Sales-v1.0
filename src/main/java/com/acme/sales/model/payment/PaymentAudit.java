package com.acme.sales.model.payment;

import com.acme.sales.model.repo.PaymentAuditRepo;
import com.acme.sales.model.services.PaymentGateway;
import com.acme.sales.model.services.PaymentGatewayTransaction;
import com.acme.sales.model.services.PaymentGatewayTransactionDetails;
import com.acme.sales.model.utils.event.Event;
import com.acme.sales.model.utils.event.EventBus;

import java.util.Date;

/**
 * Pattern : Entity
 * Regulatory requirements require all payment records to be maintained for 7 years
 * This object represents the record of the customer's payment
 */
public class PaymentAudit {

    private long     reference;         // This is the reference for the audit record for the payment
    private String   vendorReference;   // This is the reference returned by the payment gateway vndor
    private int      bookingReference;  // This is the reference to the Booking for which payment audit is created
    private Date     transactionDate;


    private String   transactionType;  // Charge, Refund

    private double transactionAmount;

    private String  cardHolderLastName;
    private String  cardHolderFirstName;

    private String   creditCardNumber;
    private String   zipCode;
    private int      expiryMonth;
    private int      expiryYear;

    // used for refund - related to original reference
    private long      relatedReference;


    /**
     * This aggregate is aware of the Repository
     */
    private  PaymentAuditRepo     paymentAuditRepo;


    /**
     * This is to create an instance of the existing record
     */
    public PaymentAudit(long reference, String vendorReference, int bookingReference, Date transactionDate, String transactionType, double transactionAmount,
                        String cardHolderLastName, String cardHolderFirstName,
                        String   creditCardNumber, String   zipCode,  int      expiryMonth, int      expiryYear,
                        long      relatedReference, PaymentAuditRepo paymentAuditRepo) {

        this.reference = reference;
        this.vendorReference = vendorReference;
        this.bookingReference = bookingReference;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.cardHolderLastName = cardHolderLastName;
        this.cardHolderFirstName = cardHolderFirstName;
        this.creditCardNumber = creditCardNumber;
        this.zipCode = zipCode;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.relatedReference = relatedReference;
        this.paymentAuditRepo = paymentAuditRepo;
    }

    /**
     * This creates an instance of the payment audit that is used for processing the payment
     */
    public PaymentAudit(int bookingReference, String transactionType, PaymentAuditRepo paymentAuditRepo) {

        // MUST set to 0 to indicate that this is a new payment instance
        this.reference = 0;
        this.vendorReference = null;
        this.bookingReference = bookingReference;
        this.transactionType = transactionType;

        this.paymentAuditRepo = paymentAuditRepo;
    }

    /**
     * Checks if the instance represents an already processed payment
     * @return
     */
    public boolean isAlreadyProcessed(){
        return (reference != 0);
    }

    /**
     * Process Payment
     * This function MUST be atomic
     *
     * NOTE: This implementation is NOT atomic as it is for simplicity/demonstration purposes only
     */
    public long processPayment(String creditCardNumber,  int expiryMonth, int expiryYear, String zipCode, double amount,
                               String cardHolderLastName, String cardHolderFirstName,
                               PaymentGateway paymentGateway) {

        if(isAlreadyProcessed()){
            // This represents an already processed payment
            // throw an Exception
            return -1;
        }

        // Save the credit card information in the audit
        this.cardHolderFirstName = cardHolderFirstName;
        this.cardHolderLastName = cardHolderLastName;
        this.creditCardNumber = creditCardNumber;
        this.expiryYear = expiryYear;
        this.expiryMonth = expiryMonth;
        this.zipCode = zipCode;

        // Call the Payment Service
        PaymentGatewayTransaction  gatewayTxn =  paymentGateway.processPayment(creditCardNumber,expiryMonth, expiryYear, zipCode, amount);

        // Get the details of the transaction
        PaymentGatewayTransactionDetails  txnDetails = paymentGateway.getTransactionDetails(gatewayTxn.reference);

        // Set it up in the aggregate
        this.vendorReference = txnDetails.reference;
        this.transactionType = txnDetails.transactionType;
        this.transactionDate = txnDetails.transactionDate;

        // Aggregate Saves itself
        this.reference = paymentAuditRepo.add(this);
        if(this.reference > 0){

            // Raise the Event
            Event event = new PaymentReceived(this);
            EventBus.raise(event);

            return this.reference;

        } else {
            // There is a problem - someone need to be notified "Payment processed & Payment Audit could NOT be created !!!!

            return -1;
        }
    }

    /**
     * Process Refund
     * This function MUST be atomic
     *
     * NOTE: This implementation is NOT atomic as it is for simplicity/demonstration purposes only
     *
     * May be invoked ONLY for an existing payment i.e., there is a valid vendor reference
     */
    public long processRefund(double amount, PaymentGateway paymentGateway) {

        // Check if valid vendorReference
        if(!isAlreadyProcessed()){
            // throw an exception
            return -1;
        }

        // Refund amount must be < refund requested
        if(amount > this.transactionAmount){
            return -1;
        }

        // 1. Create a clone of this object
        //    newPaymentAudit
        //    newPaymentAudit.relatedReference = this.reference

        // 2. Execute the processRefund on paymentGateway

        // 3. Update the PaymentAudit object
        //    Set the received transaction details on newPaymentAudit

        // 4. Save the newPaymentAudit via Repo

        // 5. If Successful - raise the PaymentRefund event

        // 6. If Failed - someone need to be informed and action needs to be taken

        return 0;
    }

    // Getters
    public long getReference() {
        return reference;
    }

    public int getBookingReference() {
        return bookingReference;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}
