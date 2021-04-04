package com.acme.sales.model.tests.fake.repo;

import com.acme.sales.model.VacationPackage;
import com.acme.sales.model.repo.VacationPackageRepo;

import java.util.ArrayList;

public class VacatonPackageRepoFake implements VacationPackageRepo {

    // Collection representing a DB
    private ArrayList<VacationPackage>  collection = new ArrayList<>();

    @Override
    public boolean add(VacationPackage vacationPackage) {

        // Add to collection
        // Ideally we should be checking if the package already exists

        collection.add(vacationPackage);

        return true;
    }

    @Override
    public VacationPackage get(String name) {
        // Loop through the collection
        for(VacationPackage vacationPackage : collection){
            if(vacationPackage.getName().equalsIgnoreCase(name)) {
                return   vacationPackage;
            }
        }
        return null;
    }

    @Override
    /**
     * Finds for + - 1 one day
     */
    public ArrayList<VacationPackage> get(String destination, int numberNights) {
        ArrayList<VacationPackage>  list = new ArrayList<>();

        for(VacationPackage vacationPackage : collection){
            if(vacationPackage.getName().equalsIgnoreCase(destination) ||
                    (( vacationPackage.getNumberOfNights() >= (numberNights - 1) )) &&
                            ( vacationPackage.getNumberOfNights() >= (numberNights + 1) )) {
                list.add(vacationPackage);
            }
        }
        return list;
    }

    @Override
    public boolean remove(String name) {
        // Loop through the collection
        for(VacationPackage vacationPackage : collection){
            if(vacationPackage.getName().equalsIgnoreCase(name)) {
                collection.remove(vacationPackage);
                return   true;
            }
        }
        return false;
    }
}
