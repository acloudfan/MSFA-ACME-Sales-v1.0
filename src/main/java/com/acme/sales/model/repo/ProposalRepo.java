package com.acme.sales.model.repo;

import com.acme.sales.model.Proposal;

import java.util.ArrayList;

public interface ProposalRepo {
    /**
     * Create | Update vacation package in sales system
     * @return
     */
    public boolean add(Proposal proposal);

    /**
     * Get vacation package using name
     * @return
     */
    public Proposal get(int  reference) ;

    /**
     * High level query function for pulling Proposals created for a customer
     * @return
     */
    public ArrayList<Proposal> getCustomerProposals(int customerReference);

    /**
     * Similar to above but allows to restict the number of proposals to recent "number" of proposals
     * e.g., request for "last 5 proposals"
     * @return
     */
    public ArrayList<Proposal> getCustomerProposals(int customerReference,int number);

    /**
     * Remove the vacation package
     */
    public boolean remove(int  reference);
}
