package com.acme.sales.model.payment;

import com.acme.sales.model.utils.event.Event;

/**
 * Represents an event for "PaymentReceived"
 */

public class PaymentReceived extends Event {

    static public  final String EVENT_PAYMENT_RECEIVED = "PaymentReceived";


    public PaymentReceived(PaymentAudit paymentAudit ) {
        super(EVENT_PAYMENT_RECEIVED, paymentAudit);
    }

    public PaymentAudit getPaymentAudit(){
        return (PaymentAudit) payload;
    }
}
