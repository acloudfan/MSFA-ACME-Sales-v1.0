package com.acme.sales.model.tests.cqrs.v2;

import com.acme.sales.model.cqrs.v2.query.ProposalQuery;
import org.json.JSONObject;

/**
 * Test the v2 ProposalQuery implementation
 */
public class TestQuery {

    // Main function
    public static void main(String[] args){

        // This is to suppress the MongoDB log messages
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        // 1. Create proposal
        ProposalQuery query = new ProposalQuery();
        int proposalId = 1;
        int customerId = 1;

        try {

            // 2. Create an instance of Proposal Query class
//            String result = query.getProposal(proposalId);
            String result = query.getProposalForCustomer(customerId);

            // 3. Pretty print JSON
            if (result.equals("{}")) {
                System.out.println("NO proposal found for proposal_id="+proposalId);
            } else {
                System.out.println("Query result:" );
                prettyPrint(result);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Pretty prints the JSON
    private static void prettyPrint(String json){
//        System.out.println(json);
        JSONObject jsonObject = new JSONObject(json);
        System.out.println(jsonObject.toString(4));
    }
}

