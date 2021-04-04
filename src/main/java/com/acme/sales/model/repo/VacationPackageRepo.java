package com.acme.sales.model.repo;

import com.acme.sales.model.VacationPackage;

import java.util.ArrayList;

public interface VacationPackageRepo {
    /**
     * Create | Update vacation package in sales system
     * @return
     */
    public boolean add(VacationPackage vacationPackage);

    /**
     * Get vacation package using name
     * @return
     */
    public VacationPackage get(String name) ;

    /**
     * Get the vacation packages based on common criteria
     * @return
     */
    public ArrayList<VacationPackage> get(String destination, int numberNights);


    /**
     * Remove the vacation package
     */
    public boolean remove(String name);

}
