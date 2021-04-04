package com.acme.sales.model.booking;

/**
 * This is to mange the reservation of the hotel
 */
public class HotelReservation extends Reservation {

    public HotelReservation(String provider, String contractCode){

        super(ReservationTypes.HOTEL, provider, contractCode);
    }

    private HotelReservation(ReservationTypes typ, String provider, String contractCode){

        super(typ, provider, contractCode);
    }

    @Override
    public boolean reserve(){
        // Successful reservation will set the reservationReference
        reservationReference =  "fake-hotel-reservation-ref";

        // At this time the status will be CONFIRMED

        return true;
    }

    @Override
    public boolean cancel() {
        // Set the cancellation reference
        cancellationReference = "fake-hotel-cancellation-ref";

        return true;
    }

    @Override
    public HotelReservation createClone() {
        HotelReservation hotelReservation = new HotelReservation(ReservationTypes.HOTEL, this.provider, this.contractCode);

        hotelReservation.setupDates(startDate, endDate);
        return hotelReservation;
    }
}
