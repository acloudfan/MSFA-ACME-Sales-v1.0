package com.acme.sales.model.cqrs.v3.command.events;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;

/**
 * This is a scheduled event processor job that continuously try to send the unsent events to MQ
 */
public class EventsProcessor implements  Runnable{

    // Wakes up every 10 seconds
    public final static long   SLEEP_TIME = 10000;

    // Instance of PubSubService for publishing the message
    private PubSubService pubSubService;

    // Uses ProposalEventRepo for getting unprocessed events
    private ProposalEventRepo proposalEventRepo = new ProposalEventRepo();

    // Instance of PubSubService injected by way of constructor
    public EventsProcessor(PubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }


    /**
     * Executed in a thread
     */
    @Override
    public void run() {
        while(true) {
            // 1. Get the unprocessed events
            ArrayList<ProposalEvent> unsentEvents = proposalEventRepo.getUnprocessed();

            // 2. Print the count of un processed events
            System.out.println("Unprocessed event count = "+unsentEvents.size());

            if (unsentEvents.size() > 0) {
                // 3. Process the events
                for(ProposalEvent event : unsentEvents) {
                    processEvent(event);
                }
            }

            // 4. Sleep for the specified time
            try{Thread.sleep(SLEEP_TIME);}catch(Exception e){}
        }
    }

    /**
     * Publishes the event and then update the status
     * @param event
     */
    private void processEvent(ProposalEvent event){

        // 1. Decode from Base64
        String payload = new String(Base64.getDecoder().decode(event.payloadBase64));
        int proposalId = event.proposal_id;

        // 2. Update event payload & Print the event payload to console
        JSONObject jsonObject = new JSONObject(payload);
        jsonObject.getJSONObject("payload").getJSONObject("proposal").put("proposal_id", proposalId);
        payload = jsonObject.toString(4);
        System.out.println(payload);

        // 3. Publish the event
        try {
            pubSubService.start();

            pubSubService.publish(payload);

            // Mark the status updated
            // This will NOT happen if there is a failure in publish
            // 4. Mark the event published
            proposalEventRepo.markEventProcessed(event.event_id);

        } catch (MessagingException me) {
            me.printStackTrace();
        }finally{
            try{pubSubService.stop();}catch(Exception e){}
        }
    }


}
