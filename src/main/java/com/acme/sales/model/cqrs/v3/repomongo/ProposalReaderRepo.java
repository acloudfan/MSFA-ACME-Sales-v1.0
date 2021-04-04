package com.acme.sales.model.cqrs.v3.repomongo;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

public class ProposalReaderRepo extends com.acme.sales.model.cqrs.v2.repomongo.ProposalReaderRepo {
    /**
     * Ideally for performance we will create an index on the guid field
     * @return   true if event with the specified GUID exist
     */
    public boolean eventGUIDExists(String eventGuid){
        boolean flag = false;

        Bson bson =  Filters.eq("guid",eventGuid);

        long count = getDocumentCount(COLECTION_PROPOSALS_EVENT_STORE, bson);
        System.out.println("Count="+count+"  "+bson);
        return (count > 0);
    }
}
