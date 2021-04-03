package com.acme.sales.model.tests;

import com.acme.sales.model.Address;
import com.acme.sales.model.Customer;
import com.acme.sales.model.Proposal;
import com.acme.sales.model.VacationPackage;
import com.acme.sales.model.booking.AirlineReservation;
import com.acme.sales.model.booking.BookingConfirmation;
import com.acme.sales.model.booking.HotelReservation;
import com.acme.sales.model.booking.Reservation;
import com.acme.sales.model.repo.BookingConfirmationRepo;
import com.acme.sales.model.repo.CustomerRepo;
import com.acme.sales.model.repo.ProposalRepo;
import com.acme.sales.model.repo.VacationPackageRepo;
import com.acme.sales.model.tests.fake.repo.BookingConfirmationRepoFake;
import com.acme.sales.model.tests.fake.repo.CustomerRepoFake;
import com.acme.sales.model.tests.fake.repo.ProposalRepoFake;
import com.acme.sales.model.tests.fake.repo.VacatonPackageRepoFake;
import com.acme.utils.DateUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RepoCollectionsTest {

    // Repo interfaces
    public static CustomerRepo customerRepo;
    public static VacationPackageRepo vacationPackageRepo;
    public static ProposalRepo proposalRepo;
    public static BookingConfirmationRepo bookingConfirmationRepo;

    /**
     * Generate the fake data in static initializer
     */
    static  {
        generateCustomer();
        generateVacationPackage();
        generateProposal();
        generateBookingConfirmation();
    }

    /**
     * Sets up the Fake Customer Collection
     */
    private static void generateCustomer(){
        // Add some customers
        customerRepo = new CustomerRepoFake();

        Address address = new Address("123 mai st","","Kent","OH","92999",true);
        Customer  customer = new Customer("Micahel","","Wang",address,"2135551212","michael@wang.com");
        // This will be the primary key in the DB
        customer.setReference(1000);
        customerRepo.add(customer);

        address = new Address("42 east summer lane","","Pesadinia","CA","90210",true);
        customer = new Customer("Jane","","Tito",address,"9185556171","jane@toto.com");
        customer.setReference(1001);
        customerRepo.add(customer);

        address = new Address("500 Starburst Court","Apartment 2C","Houston","TX","76122",true);
        customer = new Customer("Mina","","Bhind",address,"5515551616","mina@bhind.com");
        customer.setReference(1002);
        customerRepo.add(customer);
    }

    /**
     * Sets the fake vacation packages
     */
    private static void generateVacationPackage(){
        vacationPackageRepo = new VacatonPackageRepoFake();

        // BAH3NIGHTHOTELAIR
        ArrayList<Reservation> holders = new ArrayList<>();
        // Add the hotel & airline reservation holder
        holders.add(new HotelReservation("BestWestern","H1234"));
        holders.add(new AirlineReservation("United Airlines","A4567"));
        VacationPackage vacationPackage = new VacationPackage("BAH3NIGHTHOTELAIR","3 Nights Stay in a 3 star Hotel. Door to dor shuttle included. Airfare covered.",
                3, VacationPackage.vacationPackageType.HOTEL_AIR,547.98,new Date(2021,01,31),true,false,"bahamas",
                holders);
        vacationPackageRepo.add(vacationPackage);

        // FLKEYS5NIGHTHOTELAIRRENTAL
        holders = new ArrayList<>();
        holders.add(new HotelReservation("Marriott Key West","H1999"));
        holders.add(new AirlineReservation("American Airlines","A9122"));
        vacationPackage = new VacationPackage("FLKEYS5NIGHTHOTELAIRRENTAL","5 Nights Stay in a 4 star Hotel. Rental car pickup at airport. Airfare covered.",
                5, VacationPackage.vacationPackageType.HOTEL_AIR_CAR,800.98,new Date(2021,01,31),true,false,"florida",
                holders);
        vacationPackageRepo.add(vacationPackage);
    }

    /**
     * Sets up the fake proposals
     */
    private static void generateProposal() {
        proposalRepo = new ProposalRepoFake();

        VacationPackage vacationPackage = vacationPackageRepo.get("BAH3NIGHTHOTELAIR");
        Proposal proposal = new Proposal(5000, 1000,vacationPackage,new Date());
        // This package has 1 HOTEL reservation and 1 AIRLINE reservation
        proposal.setupReservationDates(0, new Date(), new Date());
        proposal.setupReservationDates(1, new Date(), new Date());
        proposalRepo.add(proposal);

        vacationPackageRepo.get("FLKEYS5NIGHTHOTELAIRRENTAL");
        proposal = new Proposal(5051,1000,vacationPackage,new Date());
        proposalRepo.add(proposal);

    }

    /**
     * Setup some BookingConfirmation
     */
    private static void generateBookingConfirmation(){
        bookingConfirmationRepo = new BookingConfirmationRepoFake();

        Proposal proposal = proposalRepo.get(5000);
        BookingConfirmation bookingConfirmation = new BookingConfirmation(6000, proposal);
        bookingConfirmationRepo.add(bookingConfirmation);
    }

    /**
     * Test function
     * @param args
     */
    public static void main(String[] args){

        // =======================================================
        // Customer calls in - Sales agent uses customer's phone number to pull customer information
        System.out.println("======= Customer calls - Sales agent pulls the cust info using phone number ======");
        Customer customer = customerRepo.get(null, "2135551212");
        System.out.println("Customer="+customer);

        // ========================================================
        // Sales agent pull info for Vacation packages & susgests "3 Nights bahamas package"
        System.out.println("\n======= Sales agent suggests the '3 Nights Bahamas' package ======");
        VacationPackage vacationPackage = vacationPackageRepo.get("BAH3NIGHTHOTELAIR");
        System.out.println("Vacation Package="+vacationPackage);

        // ========================================================
        // Customer likes the package so - Sales agent creates the proposal
        System.out.println("\n======= Customer likes so Agent created the Proposal ======");
        Proposal proposal = new Proposal(5002, 1000,vacationPackage,new Date());


        // Sales Agent sets the dates for Hotel - Check in Date = March 15, 2021 Checkout = March 17, 2021
        DateUtility dateUtility = new DateUtility();
        proposal.setupReservationDates(0,
                dateUtility.create(Calendar.MARCH, 15, 2021),
                dateUtility.create(Calendar.MARCH, 17, 2021));

        // Sales agent sets up the flights
        AirlineReservation  airlineReservation = (AirlineReservation) proposal.getReservationAtIndex(1);
        // United flight UA431 from Newark/NJ airport at 8:30 AM
        airlineReservation.setupOriginFlightInformation(dateUtility.create(Calendar.MARCH, 15, 2021,8,30),
                "EWR",
                "UA4231");
        // United flight UA1324 from Bahamas International airport at 4:30 PM
        airlineReservation.setupReturnFlightInformation(dateUtility.create(Calendar.MARCH, 17, 2021, 16, 30),
                "BAH",
                "UA1324");

        proposalRepo.add(proposal);

        System.out.println("Proposal="+proposal);

        // ============================================================
        // Sales agent MAY create additional proposals but customer commits to the proposal
        // so Sales agent creates the Booking Confirmation with that proposal
        System.out.println("\n======= Customer commits to proposal to a Booking Confirmation is created ======");
        BookingConfirmation bookingConfirmation = new BookingConfirmation(928, proposal);
        bookingConfirmationRepo.add(bookingConfirmation);
        System.out.println("Booking Confirmation (PENDING_PAYMENT) = "+bookingConfirmation);
    }
}
