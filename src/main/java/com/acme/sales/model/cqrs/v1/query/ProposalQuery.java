package com.acme.sales.model.cqrs.v1.query;

import com.acme.sales.model.Customer;
import com.acme.sales.model.Pax;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.VacationPackage;
import com.acme.sales.model.cqrs.QueryException;
import com.acme.sales.model.cqrs.v1.repojdbc.CustomerRepo;
import com.acme.sales.model.cqrs.v1.repojdbc.ProposalRepo;

import java.util.ArrayList;

public class ProposalQuery {

    /**
     * Get specific proposal
     * @param proposalId
     * @return   JSON with proposal & customer information
     * @throws QueryException
     */
    public String getProposal(int proposalId) throws QueryException {

        // 1. Create the proposal repository
        ProposalRepo proposalReaderRepo = new ProposalRepo();

        try {

            // 2. Get proposal information
            Proposal proposal = proposalReaderRepo.get(proposalId);

            // 3. Return empty JSON if not found
            if(proposal == null) return "{}";

            // 4. Get the customer information
            int customerReference = proposal.getCustomerReference();
            // 4.1 Create the customer repository
            CustomerRepo customerRepo = new CustomerRepo();
            Customer customer = customerRepo.get(customerReference);


            // 5. Create the JSON response to be returned to the caller
            String json = "{"+System.lineSeparator();
            json += "'customer' : "+convertToJSON(customer)+","+System.lineSeparator();
            json += "'proposal' : "+convertToJSON(proposal)+System.lineSeparator();

            json += "}";

            return json;

        } catch (Exception e){
            throw new QueryException(e);
        }

    }

    /**
     * Returns all proposals  for the customer
     * @param customerId
     * @return   JSON with customer and proposals
     * @throws QueryException
     */
    public String getProposalForCustomer(int customerId) throws QueryException {

        // 1. Create instances of respos (may be injected in real implementation)
        ProposalRepo proposalRepo = new ProposalRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        try {

            //  2. Get customer info
            Customer customer = customerRepo.get(customerId);

            // 3. Get proposals for customer
            ArrayList<Proposal> proposals = proposalRepo.getCustomerProposals(customerId);

            // 4. Convert the model to JSON expected by the caller
            String customerJSON = convertToJSON(customer);
            String proposalsJSON = "[";

            for(Proposal proposal : proposals){
                proposalsJSON += convertToJSON(proposal)+","+System.lineSeparator();
            }

            proposalsJSON += "]";

            String json = "{customer: "+customerJSON+",";
            json += "proposals: "+proposalsJSON + "}";

            return  json;

        } catch (Exception e){
            throw new QueryException(e);
        }


    }

    /**
     * Converts the proposal to JSON string
     */
    private String convertToJSON(Proposal proposal){

        int proposalReference = proposal.getReference();
        int custReference = proposal.getCustomerReference();
        ArrayList<Pax> paxs =  proposal.getPassengers();
        VacationPackage vacationPackage = proposal.getVacationPackage();

        String json = "{";

        json += "'proposalId' : "+proposalReference + "," + System.lineSeparator();
        json += "'package' : '"+vacationPackage.getName() + "'," + System.lineSeparator();
        json += "'pax' : [";
        for(Pax pax : paxs){
            String s = "{ 'firstName' : '"+pax.fName+"', 'lastName' : '"+pax.lName+"', 'age' : "+pax.age+"}";
            json += s +","+System.lineSeparator();
        }
        json += "]";

        json += "}";

        return json;
    }

    /**
     * Converts the customer to JSON string
     */
    private String convertToJSON(Customer customer){

        String email = customer.getEmail();
        String phone = customer.getPhoneNumber();

        String json = "{";

            json += "'reference' : "+customer.getReference()+","+System.lineSeparator();
            json += "'firstName:' : '"+customer.fName+"', 'middleName:' : '"+customer.mName+"', 'lastName:' : '"+customer.lName+"',"+System.lineSeparator();
            json += "'email' : '"+ email+"',"+System.lineSeparator();
            json += "'phoneNumber' : '"+phone+"'"+System.lineSeparator();

        json += "}";

        return json;
    }

}

