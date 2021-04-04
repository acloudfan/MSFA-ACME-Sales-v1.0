package com.acme.sales.model.cqrs.v2.repomongo;

import com.acme.infra.mongodb.MongoDBBase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

/**
 * This Repo interacts with MongoDB
 *
 * READ Side does not depend on the WRITE side model i.e., ProposalRepo interface !!!
 */
public class ProposalReaderRepo extends MongoDBBase {

    // Collection for events
    public  static final String COLECTION_PROPOSALS_EVENT_STORE="proposaleventstore";

    // Collection for proposals
    public  static final String COLECTION_PROPOSALS="proposals";

    /**
     * Adds the event JSON to the events collection
     * @param eventJSON
     */
    public void addProposalEvent(String eventJSON){
        executeInsert(COLECTION_PROPOSALS_EVENT_STORE, eventJSON);
    }

    /**
     * Adds the JSON to query data store
     * @param proposalJSON
     */
    public void  addProposal(String proposalJSON){
        executeInsert(COLECTION_PROPOSALS, proposalJSON);
    }

    /**
     * Gets the specific proposal
     * @param proposalId
     * @return
     */
    public String getProposal(int proposalId){

        String json ="{count: 0}";

        // 1. Create the filter string
        Bson bson =  Filters.eq("proposal_id",proposalId);

        FindIterable iterable = find(COLECTION_PROPOSALS, bson);

        ArrayList<Document> docs = new ArrayList<Document>();

        iterable.into(docs);

       if(docs.size() == 0){
           return json;
       }

       json = "{ count: 1, result: "+docs.get(0).toJson() +"}";

       return json;
    }

    /**
     * Gets the collection of proposals for a specified customer
     * @param customerId
     * @return
     */
    public String getProposalsForCustomer(int customerId){

        String json ="{customer_id: "+customerId+", proposals: []}";

        Bson bson =  Filters.eq("customer_id",customerId);

        // System.out.println("MongoDB Find Criteria: "+bson);

        FindIterable iterable = find(COLECTION_PROPOSALS, bson);

        ArrayList<Document> docs = new ArrayList<Document>();

        iterable.into(docs);

        if(docs.size() == 0){
            return json;
        }

        json ="{customer_id: "+customerId+", proposal_count: "+ docs.size()+", proposals: [";
        boolean addedFirst = false;

        for(Document doc : docs) {
            if(!addedFirst) {
                addedFirst = true;
            } else {
                json += ", ";
            }
            json += doc.toJson();

        }
        json += "]}";

        return json;
    }


}
