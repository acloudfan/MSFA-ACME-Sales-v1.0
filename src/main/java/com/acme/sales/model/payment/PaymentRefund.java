package com.acme.sales.model.payment;

import com.acme.sales.model.utils.event.Event;

/**
 * Represents an event for "PaymentCancelled"
 */

public class PaymentRefund extends Event {

    public PaymentRefund(PaymentAudit paymentAudit ) {
        super("PaymentCancelled", paymentAudit);
    }

    public PaymentAudit getPaymentAudit(){
        return (PaymentAudit) payload;
    }
}
