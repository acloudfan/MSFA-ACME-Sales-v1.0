package com.acme.sales.model.booking;

import com.acme.sales.model.payment.PaymentAudit;
import com.acme.sales.model.repo.BookingConfirmationRepo;
import com.acme.sales.model.utils.event.Event;
import com.acme.sales.model.utils.event.EventHandler;

/**
 * Represents the Event Handler for PaymentReceived
 */
public class PaymentReceived implements EventHandler  {

    private BookingConfirmationRepo  bookingConfirmationRepo;

    /**
     * Needs access to the BookingConfirmation repo
     */
    public PaymentReceived(BookingConfirmationRepo  bookingConfirmationRepo){
        this.bookingConfirmationRepo = bookingConfirmationRepo;
    }

    @Override
    public void handle(Event event) {
        // This is where the event is handeled

        // 1. Event payload is a PaymentAudit type
        PaymentAudit paymentAudit = ((com.acme.sales.model.payment.PaymentReceived)  event).getPaymentAudit();

        // 2. Get the booking confirmation
        int  bookingReference = paymentAudit.getBookingReference();

        // 3. Pull the booking confirmation from the repo
        BookingConfirmation bookingConfirmation = bookingConfirmationRepo.get(bookingReference);

        // 4. Update the booking reference with the
        //    This will update the state of BookingConfirmation to PENDING_RESERVATION
        bookingConfirmation.setPaymentConfirmationReference(paymentAudit.getReference(), paymentAudit.getTransactionDate());
        bookingConfirmationRepo.add(bookingConfirmation);

        // 5. Call the Reservation function
        System.out.println("===== Handler : Kick off the Reservations ===");
        bookingConfirmation.reserve();
    }

}
