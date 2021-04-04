package com.acme.sales.model.cqrs.v2.command;

import com.acme.infra.rabbitmq.PubSubService;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.cqrs.v2.repojdbc.CustomerRepo;
import com.acme.sales.model.cqrs.v2.repojdbc.ProposalRepo;
import com.acme.sales.model.utils.event.messaging.MessagingException;
import org.json.JSONObject;

import java.util.UUID;

public class CreateProposalCommand extends com.acme.sales.model.cqrs.v1.command.CreateProposalCommand {

    /**
     * 1. UPDATE the URL for RabbitMQ
     * 2. Ensure that the Exchange, Topic & Queue are in place
     */
        public final String AMQP_URL ="provide the AMQP URL";

    public final String AMQP_EXCHANGE ="acme.sales.topic";
    public final String AMQP_TOPIC = "proposal.update";

    /**
     * Constructor
     */
    public CreateProposalCommand(Proposal proposal) {
        super(proposal);
    }

    /**
     * Overrides the v1 executePostProcessing function
     *
     * @param proposaIdJSON
     */
    @Override
    public void executePostProcessing(String proposaIdJSON) {

        System.out.println("Post processing started.");

        // 1. Extract the proposal_id
        JSONObject jsonObject = new JSONObject(proposaIdJSON);
        int proposalId = jsonObject.getInt("proposal_id");

        // 2. Prepare the proposal data to be published
        String proposalJSON = "{}";
        String customerJSON = "{}";

        // 3. Create instnce of repo
        try {

            // 4. Get proposal info
            ProposalRepo  proposalRepo = new ProposalRepo();
            proposalJSON = proposalRepo.getProposalJSON(proposalId);


            // 5. Get customer ID from proposal
            jsonObject = new JSONObject(proposalJSON);
            int customerId = jsonObject.getInt("customer_id");

            // 6. Get the customer info
            CustomerRepo customerRepo = new CustomerRepo();
            customerJSON = customerRepo.getCustomerJSON(customerId);

        } catch(Exception e){
            // THIS WILL BE BAD as message will be lost !!!
            e.printStackTrace();
        }

        // 7. Setup the event payload
        String eventPayload = "{ customer: "+customerJSON+", proposal: "+proposalJSON+"}";

        // 8. Set up the event data
        String data ="{action: 'created', timestamp: "+ System.currentTimeMillis() + ", guid : '"+ UUID.randomUUID() +"'";
        data += ", source: 'CreateProposalCommand'";
        data += ", payload: "+eventPayload+" }";

        // 9. This is where we will publish a message
        PubSubService pubSubService = new PubSubService(AMQP_URL, AMQP_EXCHANGE, AMQP_TOPIC);

        // 9. Publish the event
        try {
            pubSubService.start();

            pubSubService.publish(data);

        } catch (MessagingException me) {
            me.printStackTrace();
        }finally{
            try{pubSubService.stop();}catch(Exception e){}
        }
    }


}
