package com.acme.sales.model.cqrs.v3.command;

import com.acme.sales.model.Proposal;
import com.acme.sales.model.cqrs.Command;
import com.acme.sales.model.cqrs.CommandException;
import com.acme.sales.model.cqrs.v3.repojdbc.ProposalRepo;

public class CreateProposalCommand implements Command {

    private Proposal proposal;

    public CreateProposalCommand(Proposal proposal){
        this.proposal = proposal;
    }

    @Override
    public void process() throws CommandException {

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
