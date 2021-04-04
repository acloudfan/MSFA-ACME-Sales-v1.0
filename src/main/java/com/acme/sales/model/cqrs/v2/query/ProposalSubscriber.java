package com.acme.sales.model.cqrs.v2.query;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.cqrs.v2.repomongo.ProposalReaderRepo;
import com.acme.sales.model.utils.event.messaging.MessageHandler;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import org.json.JSONObject;

/**
 * Subscribes to the proposal messages
 *
 * ACL = Anti Corruption Layer
 *
 * It may be a standalone process that acts as the ACL
 *
 * The received message may be transformed to a Reader compliant model
 *
 */
public class ProposalSubscriber {

    protected final PubSubService pubSubService;

    // Takes an instance of PubSubService
    public ProposalSubscriber(PubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }

    /**
     * Subscribes to the events - on receieving the event does the following:
     * 1. Updates the proposals event store
     * 2. Extracts the proposal
     * 3. Adds the proposal
     */
    public void start() throws MessagingException{

        // 1. SETUP Handler for the message
        MessageHandler messageHandler = (message) -> {

            // 1. Add the event to the event store
            ProposalReaderRepo repo = new ProposalReaderRepo();
            repo.addProposalEvent(message);

            // 2. Process the event
            processProposalEvent(message);

        };

        // 2. Start the subscriber
        pubSubService.start();
        pubSubService.subscribe(messageHandler);

        // 3. System
        System.out.println("Subscriber started ... waiting for messages");
    }

    /**
     * Processes the received event as per the needs
     *
     * Lets ASSUME that this subscriber is interested ONLY in the proposal part of the event
     */
    protected void processProposalEvent(String eventJSON){

        //1. Create the JSON object from JSON string
        JSONObject jsonObject = new JSONObject(eventJSON);
        String action = jsonObject.getString("action");

        //2. Print the message information
        System.out.println("Received Proposal Event | "+ action+" | proposal_id="+jsonObject.getJSONObject("payload").getJSONObject("proposal").getInt("proposal_id"));

        //3. Take care of the various Proposal action
        if(action.equals("created")){
            // New proposal is created so insert

            // 1. Extract the new proposal from the event data
            JSONObject proposalJSONObject = jsonObject.getJSONObject("payload").getJSONObject("proposal");

            JSONObject transformedJSON = aclTransform(proposalJSONObject);

            ProposalReaderRepo  repo = new ProposalReaderRepo();

            repo.addProposal(proposalJSONObject.toString());

        } else if(action.equals("updated")){

            // LEFT as an exercise
            // For this there will be a need to create indexes on MongoDB
            // Beyond the scope of this exercise

        } else if(action.equals("removed")){

            // LEFT as an exercise
            // For this there will be a need to create indexes on MongoDB
            // Beyond the scope of this exercise

        }

    }

    /**
     * Anti Corruption Layer Transformation
     * In REAL implementation this would transform the WRITE model to READ model
     *
     * In this implementation NO model transformation carried out to keep things simple
     */
    private JSONObject aclTransform(JSONObject received){
        return received;
    }
}
