package com.acme.sales.model.booking;

public class RentalCarReservation extends Reservation {


    public RentalCarReservation(String provider, String contractCode) {
        super(ReservationTypes.CAR_RENTAL, provider, contractCode);
    }

    private RentalCarReservation(ReservationTypes typ,String provider, String contractCode) {
        super(typ, provider, contractCode);
    }


    @Override
    public boolean reserve(){
        // Successful reservation will set the reservationReference
        reservationReference =  "fake-car-reservation-ref";
        return true;
    }

    @Override
    public boolean cancel() {
        // Set the cancellation reference
        cancellationReference = "fake-car-cancellation-ref";

        return true;
    }

    @Override
    public RentalCarReservation createClone() {
        RentalCarReservation rentalCarReservation = new RentalCarReservation(ReservationTypes.CAR_RENTAL ,this.provider, this.contractCode);
        rentalCarReservation.setupDates(startDate, endDate);
        return rentalCarReservation;
    }
}
