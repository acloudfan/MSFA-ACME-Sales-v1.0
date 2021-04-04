package com.acme.sales.model.tests.event.audit;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.booking.BookingConfirmation;
import com.acme.sales.model.payment.PaymentAudit;
import com.acme.sales.model.payment.PaymentReceived;
import com.acme.sales.model.repo.BookingConfirmationRepo;
import com.acme.sales.model.repo.PaymentAuditRepo;
import com.acme.sales.model.services.PaymentGateway;
import com.acme.sales.model.tests.PaymentGatewayProcessor;
import com.acme.sales.model.tests.RepoCollectionsTest;
import com.acme.sales.model.tests.fake.repo.PaymentAuditRepoFake;
import com.acme.sales.model.utils.event.EventBus;
import com.acme.sales.model.utils.event.messaging.MessagingService;

import java.util.HashMap;

/**
 * Demonstrates the working of the PaymentAudit functionality
 */
public class PaymentAuditTest {

    // MUST replace this URL to your instance on RabbitMQ
    final private static String AMQP_URL="amqps://lolgkfrt:JrxrfAVFIzKhztFY4NCLTtyRwvEIaFEX@shark.rmq.cloudamqp.com/lolgkfrt";

    private static MessagingService messagingService;

    public static void main(String[] args){

        // 1. Get the reference to Booking confirmation repo (Fake)
        BookingConfirmationRepo bookingConfirmationRepo = RepoCollectionsTest.bookingConfirmationRepo;

        // Uncomment this ONLY if you have setup the Exchange on Rabbit MQ
        // Used in the Lectures on "Code Walkthrough and Demo of BookingConfirmed Integration Event"
        setupMessaging(bookingConfirmationRepo);

        // 2. Create an instance of the Handler & Register the handler
        System.out.println("===== Register the handler ===="+com.acme.sales.model.booking.PaymentReceived.class);
        com.acme.sales.model.booking.PaymentReceived  paymentReceived =
                new com.acme.sales.model.booking.PaymentReceived(bookingConfirmationRepo);
        EventBus.register(PaymentReceived.EVENT_PAYMENT_RECEIVED, paymentReceived);

        // 3. The repo is already initialized with a Booking confirmation with reference = 928
        BookingConfirmation  bookingConfirmation = bookingConfirmationRepo.get(928);
        System.out.println("===== Get BookingConfirmation object for which customer is paying ====");
        System.out.println(bookingConfirmation);

        // Uncomment this to see the details of the BookingConfirmation object
        // System.out.println(bookingConfirmation);

        // 4. Create a fake instance of the payment gateway
        PaymentGateway paymentGateway = new PaymentGatewayProcessor();

        // 5. Create an instance of the PaymentAuditRepo
        PaymentAuditRepo paymentAuditRepo = new PaymentAuditRepoFake();

        // 6. Now lets process the payment => This should lead to processing in the Handler
        System.out.println("===== Proces Payment using the PaymentAudit.pay(...) ====");
        // New class that represents the PaymentAudit Entity
        PaymentAudit paymentAudit = new PaymentAudit(bookingConfirmation.getReference(),"CHARGE", paymentAuditRepo);
        paymentAudit.processPayment("13245555",05,2030,
                "91620",1092.23,
                "Wang","Michael",paymentGateway);

        // 7. Now compare the Before & After status of the Booking confirmation
        System.out.println(bookingConfirmation);

        // 8. Stop the messaging connection
        if(messagingService != null){
            try {messagingService.stop();} catch(Exception e){}
        }
    }

    /**
     * This is to setup the messaging on BookingConfirmationRepo
     * Demo : Used in the lectures on Integration Events
     */
    private static void setupMessaging(BookingConfirmationRepo bookingConfirmationRepo){



        final String AMQP_EXCHANGE = "acme.sales.topic";
        final String AMQP_TOPIC = "acme.sales.bookingconfirmed";

        HashMap<String, String> props = new HashMap<>();
        props.put("AMQP_URL", AMQP_URL);
        props.put("AMQP_EXCHANGE", AMQP_EXCHANGE);
        props.put("AMQP_TOPIC", AMQP_TOPIC);

        // 1. Create an instance of the MessagingSerive
        messagingService = new   PubSubService(props);

        // 2. Start the messaging service
        try {
            messagingService.start();
        }catch(Exception e){
            System.out.println("Looks like the Messaging URI is invalid - please make sure it is good!!!");
            e.printStackTrace();
            System.exit(1);
        }

        // 3. Set the Messaging Service on the BookingConfirmationRepo
        bookingConfirmationRepo.setupMessagingService(messagingService);
    }
}
