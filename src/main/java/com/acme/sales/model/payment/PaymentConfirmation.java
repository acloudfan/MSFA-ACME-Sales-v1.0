package com.acme.sales.model.payment;

import java.util.Date;

/**
 * Holds information on the payment confirmation
 */
public class PaymentConfirmation {
    /** Reference number for payment**/
    private int  referenceNumberPayment = -1;

    /** Reference number for cancellation**/
    private int  referenceNumberCancellation = -1;

    /** Date processed **/
    /** This will hold the payment date OR cancellation date **/
    private Date   processedDate;

    /**
     * Created by the Payment Gateway service when the payment is successfully processed
     * @param referenceNumberPayment
     * @param processedDate
     */
    public PaymentConfirmation(int referenceNumberPayment, Date processedDate) {
        this.referenceNumberPayment = referenceNumberPayment;
        this.processedDate = processedDate;
    }

    /**
     * Create by the Payment Gateway service when the payment is fully refunded
     * E.g., when customer cancels the booking
     */
    public PaymentConfirmation(PaymentConfirmation existingPaymentConfirmation, int referenceNumberCancellation, Date processedDate){
        // If existing payment confirmation is not paid then throw an exception
        // This should be replaced with an appropriate service defined exception - done this way to provide a simplified view
        if(!existingPaymentConfirmation.isPaid()) throw new RuntimeException("Can't create with non paid Payment confirmation !!");

        PaymentConfirmation newPaymentConfirmation = new PaymentConfirmation(existingPaymentConfirmation.referenceNumberPayment, processedDate);
        newPaymentConfirmation.referenceNumberCancellation = referenceNumberCancellation;
    }

    public void setReferenceNumberCancellation(int referenceNumberCancellation, Date date) {
        this.referenceNumberCancellation = referenceNumberCancellation;
        this.processedDate = date;
    }

    /**
     * Considered to be PAID only if the payment was received and NOT cancelled
     * @return
     */
    public boolean isPaid(){
        return (referenceNumberPayment > 0 && referenceNumberCancellation == -1);
    }

    public boolean isCancelled(){
        return (referenceNumberCancellation > 0);
    }
}
