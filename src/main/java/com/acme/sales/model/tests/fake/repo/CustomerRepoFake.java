package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.Customer;
import com.acme.sales.model.repo.CustomerRepo;
import com.acme.sales.model.utils.event.messaging.MessagingService;

import java.util.ArrayList;

public class CustomerRepoFake implements CustomerRepo {

    // In memory collection to simulate the database
    private ArrayList<Customer>  collection = new ArrayList<>();




    @Override
    public boolean add(Customer customer) {

        // Ideally a check should be made if customer is already in the list
        // If its there then replace it - for simplicity we are just adding a new customer

        collection.add(customer);

        return true;
    }


    @Override
    public Customer get(int reference) {

        // Loop through the collection
        for(Customer  customer : collection){
            if(customer.getReference() == reference) {
                return   customer;
            }
        }

        return null;
    }

    @Override
    public Customer get(String email, String phoneNumber) {
        // Loop through the collection
        for(Customer  customer : collection){
            if(customer.isCustomerEqual(email,phoneNumber) ) {
                return   customer;
            }
        }

        return null;
    }

    @Override
    public boolean remove(Customer customer) {

        // Loop through the collection
        for(Customer  customerInCollection : collection){
            if(customerInCollection.getReference() == customer.getReference()) {
                collection.remove(customer);
                return   true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(int reference) {
        // Loop through the collection
        for(Customer  customerInCollection : collection){
            if(customerInCollection.getReference() == reference) {
                collection.remove(customerInCollection);
                return   true;
            }
        }

        return false;
    }


}
