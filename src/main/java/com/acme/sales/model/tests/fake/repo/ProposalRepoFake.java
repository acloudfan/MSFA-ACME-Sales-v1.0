package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.Proposal;
import com.acme.sales.model.repo.ProposalRepo;

import java.util.ArrayList;

public class ProposalRepoFake implements ProposalRepo {
    // In memory collection to simulate the database
    private ArrayList<Proposal>  collection = new ArrayList<>();

    @Override
    public boolean add(Proposal proposal) {
        collection.add(proposal);
        return true;
    }

    @Override
    public Proposal get(int reference) {
        // Loop through the collection
        for(Proposal proposal : collection){
            if(proposal.getReference() == reference) {
                return   proposal;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Proposal> getCustomerProposals(int customerReference) {
        // Set the number to -1 and it will not match with added in next function hence all proposals returned
        return  getCustomerProposals(customerReference, -1);
    }

    @Override
    public ArrayList<Proposal> getCustomerProposals(int customerReference, int number) {
        int added = 0;
        ArrayList<Proposal>  proposals = new ArrayList<>();
        // Loop through the collection
        for(Proposal proposal : collection){
            if(proposal.getCustomerReference() == customerReference) {
                proposals.add(proposal);
                added++;
                if(added == number) return proposals;
            }
        }
        return proposals;
    }

    @Override
    public boolean remove(int reference) {
        // Loop through the collection
        for(Proposal proposal : collection){
            if(proposal.getReference() == reference) {
                collection.remove(proposal);
                return   true;
            }
        }
        return false;
    }
}
