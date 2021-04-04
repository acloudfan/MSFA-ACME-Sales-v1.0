package com.acme.sales.model.booking;

import java.util.Date;

public class AirlineReservation extends Reservation {

    /**
     * Holds information on the flights
     * ASSUMPTION in this version = ONLY direct flight - in next version we can enhance it :) but lets keep it simple
     */
    private String originFlightNumber;
    private String originAirportCode;
    private String returnFlightNumber;
    private String returnFlightAirportCode;

    public AirlineReservation(String provider, String contractCode) {
        super(ReservationTypes.AIRLINE, provider, contractCode);
    }

    private AirlineReservation(ReservationTypes typ, String provider, String contractCode) {
        super(typ, provider, contractCode);
    }

    @Override
    public boolean reserve(){
        // Successful reservation will set the reservationReference
        reservationReference =  "fake-airline-reservation-ref";

        return true;
    }

    @Override
    public boolean cancel() {
        // Set the cancellation reference
        cancellationReference = "fake-airline-cancellation-ref";

        return true;
    }

    /**
     * This is to setup the flight information - to destination
     */
    public void setupOriginFlightInformation(Date date, String originAirportCode, String originFlightNumber){
        this.startDate=date;
        this.originAirportCode=originAirportCode;
        this.originFlightNumber=originFlightNumber;
    }
    /**
     * This is to setup the flight information - to destination
     */
    public void setupReturnFlightInformation(Date date, String returnAirportCode, String returnFlightNumber){
        this.endDate=date;
        this.returnFlightAirportCode=returnAirportCode;
        this.returnFlightNumber=returnFlightNumber;
    }

    @Override
    public String getReservationReference() {
        return super.getReservationReference();
    }

    @Override
    public AirlineReservation createClone() {
        AirlineReservation airlineReservation = new AirlineReservation(ReservationTypes.AIRLINE, this.provider, this.contractCode);
        airlineReservation.setupDates(this.startDate, this.endDate);
        airlineReservation.originFlightNumber=this.originFlightNumber;
        airlineReservation.originAirportCode=this.originAirportCode;
        airlineReservation.returnFlightNumber=this.returnFlightNumber;
        airlineReservation.returnFlightAirportCode=this.returnFlightAirportCode;
        return airlineReservation;
    }

    @Override
    public String toString(){
        String str = super.toString();
        str += " Flights=***"+this.originAirportCode+":"+this.originFlightNumber;
        str += "***"+this.returnFlightAirportCode+":"+this.returnFlightNumber+"***";
        return str;
    }
}
