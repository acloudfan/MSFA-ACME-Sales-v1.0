package com.acme.sales.model.repo;

import com.acme.sales.model.Customer;

/**
 * Tactical Pattern: Repository
 * Model: Acme Sales
 * Represents a customer repository
 */

public interface CustomerRepo  {

    /**
     * Create | Update a customer
     * @param customer
     * @return
     */
    public boolean add(Customer customer);

    /**
     * Get customer using reference ID
     * @param obj
     * @return
     */
    public Customer get(int reference) ;

    /**
     * Get the customer based on email or phone number
     * @param email
     * @param phoneNumber
     * @return
     */
    public Customer get(String email, String phoneNumber);


    /**
     * Remove the customer
     */
    public boolean remove(Customer customer) ;
    public boolean remove(int  reference);
}
