package com.acme.sales.model.cqrs.v3.repojdbc;

import com.acme.sales.model.Customer;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

/**
 * Create a query with two Insert statements that both either succeed or fail
 *
 * Using PG "with" clause to create
 * https://www.postgresql.org/docs/9.1/queries-with.html
 */
public class ProposalRepo extends com.acme.sales.model.cqrs.v2.repojdbc.ProposalRepo {

    @Override
    protected String addProposal(int customerId, String packageId, ArrayList<String> pax, ArrayList<Integer> paxAge) throws SQLException {

        // 1. Create the SQL for the insert of the poroposal
        String sqlproposal = createInsertSQL(customerId, packageId, pax, paxAge);

        // 2. Create event payload + SQL Insert for event
        String sqlEvent = createEventPayloadAndSQL(customerId, packageId, pax, paxAge);

        // 3. Combine the SQLs in a 'Single Unit of Work'
        String query = "WITH p AS ( "+
                    sqlproposal +
                "), pe as (" +
                    sqlEvent +
                ") " +
                "SELECT proposal_id FROM p";

        // 4. Print the query to console
        System.out.println(query);

        // 5. Execute the SQL
        String proposalIdJSON = executeSQLUpdate(query);

        // 6. Return the response JSON that has the proposal_id
        return proposalIdJSON;
    }


    /**
     * Creates the JSON payload for the event
     */
    private String createEventPayloadAndSQL(int customerId, String packageId, ArrayList<String> pax, ArrayList<Integer> paxAge){


        JSONObject  proposalObject = new JSONObject();
        proposalObject.put("package_id", packageId);
        for(int i=0; i < pax.size(); i++){
            proposalObject.put("pax_"+i, pax.get(i));
            proposalObject.put("pax_age_"+i, paxAge.get(i));
        }

        // Get the customer data
        Customer customer = (new CustomerRepo()).get(customerId);
        JSONObject customerObject = new JSONObject();
        customerObject.put("fname", customer.fName);
        customerObject.put("mname", customer.mName);
        customerObject.put("lname", customer.lName);
        customerObject.put("email_address", customer.getEmail());
        customerObject.put("phone", customer.getPhoneNumber());

        // Payload has the customer & proposal information
        JSONObject payload = new JSONObject();
        payload.put("customer", customerObject);
        payload.put("proposal", proposalObject);

        // Full event JSON = envelope + payload
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "created");
        jsonObject.put("timestamp", System.currentTimeMillis());
        UUID guid = UUID.randomUUID();
        jsonObject.put("guid", guid.toString());
        jsonObject.put("source","CreateProposalCommand");
        jsonObject.put("payload", payload);

//        System.out.println(jsonObject.toString(4));

        // Encode event data to Base64 - prevents challenges with special characters such as quotes
        String payloadBase64 = Base64.getEncoder().encodeToString(jsonObject.toString(4).getBytes());

        // Create the SQL statement
        String sql = "INSERT INTO proposalevents(proposal_id,event_guid,payload) VALUES((SELECT proposal_id FROM p),'"+guid.toString()+"','"+ payloadBase64 + "')";

        return sql;
    }



}
