package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.payment.PaymentAudit;
import com.acme.sales.model.repo.PaymentAuditRepo;

import java.util.ArrayList;

/**
 * This is a fake repo - to demonstrate the working of the PaymentAudit
 */
public class PaymentAuditRepoFake implements PaymentAuditRepo {

    private ArrayList<PaymentAudit>  paymentAudits = new ArrayList<>();

    @Override
    public long add(PaymentAudit paymentAudit) {
        paymentAudits.add(paymentAudit);

        // Generate a fake reference number
        long fakeReference = System.currentTimeMillis();

        return fakeReference;
    }

    @Override
    public PaymentAudit get(long reference) {
        for(PaymentAudit paymentAudit : paymentAudits){
            if(paymentAudit.getReference() == reference){
                return  paymentAudit;
            }
        }
        return null;
    }
}
