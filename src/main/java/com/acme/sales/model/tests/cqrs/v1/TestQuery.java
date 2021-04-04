package com.acme.sales.model.tests.cqrs.v1;

import com.acme.sales.model.cqrs.v1.query.ProposalQuery;

public class TestQuery  {

    public static void main(String[] args){
        ProposalQuery query = new ProposalQuery();
        int proposalId = 6;
        int customerId = 1;
        try {
//            String result = query.getProposal(proposalId);
            String result = query.getProposalForCustomer(customerId);
//
            if (result == null) {
                // No proposal found
                System.out.println("NO proposal found for proposal_id="+proposalId);
            } else {
                System.out.println("Query result = " + result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

