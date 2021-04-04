package com.acme.sales.model.cqrs.v1.command;

import com.acme.sales.model.Proposal;
import com.acme.sales.model.cqrs.Command;
import com.acme.sales.model.cqrs.CommandException;
import com.acme.sales.model.cqrs.v1.repojdbc.ProposalRepo;

/**
 * Typically for complex setup you would have Command >> Aggregate >> Repo
 * To keep things simple all 3 are combined into this one class
 *
 * Using the Builder pattern
 */
public class CreateProposalCommand  implements Command {

    private Proposal proposal;

    public CreateProposalCommand(Proposal proposal){
        this.proposal = proposal;
    }

    // Adds/Updates the passenger
    public void addPax(String fname, String mname, String lname, int age){

        proposal.addPax(fname, mname, lname, age);

    }

    public void process() throws CommandException {

        /**
         * This is where:
         * 1. Transform the request to model if needed
         * 2. You would validate the request
         * 3. Implement the business logic
         * 4. Execute the instructions in the command
         */

        // 1. Create an instance of the Proposal repo   (may be injected)
        ProposalRepo writerRepo = new ProposalRepo();

        try {
                // 2. Add the proposal
                String proposalIdJSON = writerRepo.add(proposal) ;

                // 3. Do the post processing
                executePostProcessing(proposalIdJSON);

        } catch(Exception e){
            throw new CommandException(e);
        }
    }



    /**
     * This is where we can do post processing
     */
    public void executePostProcessing(String proposalIdJSON) {
        System.out.println("No post processing.");
    }
}
