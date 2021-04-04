package com.acme.sales.model.cqrs.v2.query;

import com.acme.sales.model.cqrs.QueryException;
import com.acme.sales.model.cqrs.v2.repomongo.ProposalReaderRepo;

/**
 * Provides the Query model which is independent of the Write Model
 * Leverages the MongoDB repo
 *
 * Expose same functions as v1 ProposalQuery but with a difference
 * Since this Query model is not interested in Customer details - its query result ignores customer
 */
public class ProposalQuery {

    /**
     * Gets the proposal based on proposalID
     */
    public String getProposal(int proposalId) throws QueryException {
        // 1. create the repo
        ProposalReaderRepo repo = new ProposalReaderRepo();

        // 2. get the proposal
        String json = repo.getProposal(proposalId);

        // 3. return the json
        return json;
    }

    /**
     * Performance considerations : MUST define appropriate indexes
     */
    public String getProposalForCustomer(int customerId) throws QueryException {
        // 1. create the repo
        ProposalReaderRepo repo = new ProposalReaderRepo();

        // 2. get the collection of proposals for given customer
        String json = repo.getProposalsForCustomer(customerId);

        // 3. return the json
        return json;
    }

}
