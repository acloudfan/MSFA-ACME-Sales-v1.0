package com.acme.sales.model.cqrs.v3.query;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.cqrs.v3.repomongo.ProposalReaderRepo;
import com.acme.sales.model.utils.event.messaging.MessageHandler;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import org.json.JSONObject;

public class ProposalSubscriber extends com.acme.sales.model.cqrs.v2.query.ProposalSubscriber {

    /**
     * Constructor
     * @param pubSubService
     */
    public ProposalSubscriber(PubSubService pubSubService){
        super(pubSubService);
    }

    @Override
    /**
     * Subscribes to the events - on receieving the event does the following:
     * 1. Updates the proposals event store
     * 2. Extracts the proposal
     * 3. Adds the proposal
     */
    public void start() throws MessagingException {

        // 1. SETUP Handler for the message
        MessageHandler messageHandler = (message) -> {

            // 1. Add the event to the event store
            ProposalReaderRepo repo = new ProposalReaderRepo();

            JSONObject jsonObject = new JSONObject(message);
            String eventGuid = jsonObject.getString("guid");

            if (repo.eventGUIDExists(eventGuid)) {

                System.out.println("Event with guid="+eventGuid +" Already processed. Ignoring Dup !!");

            } else {

                repo.addProposalEvent(message);

                // 2. Process the event
                processProposalEvent(message);
            }


        };

        // 2. Start the subscriber
        pubSubService.start();
        pubSubService.subscribe(messageHandler);

        // 3. System
        System.out.println("Subscriber started ... waiting for messages");
    }
}
