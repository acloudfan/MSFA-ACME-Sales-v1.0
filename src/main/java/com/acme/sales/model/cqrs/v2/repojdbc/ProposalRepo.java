package com.acme.sales.model.cqrs.v2.repojdbc;

import java.sql.SQLException;

public class ProposalRepo extends com.acme.sales.model.cqrs.v1.repojdbc.ProposalRepo {

    public String getProposalJSON(int proposalId) throws SQLException {
        // 1. Create the SQL
        String sql = "SELECT * from proposals WHERE proposal_id="+proposalId;

        // 2. Execute the SQL
        String json = executeSQL(sql, true);

        return json;

    }
}
