package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

import java.time.LocalDate;


public class ReservationRequestDto {

    private int numPeople;
    private LocalDate checkin;
    private LocalDate checkOut;


    private String customerEmail;

    public ReservationRequestDto() {

    }

    public ReservationRequestDto(int numPeople, LocalDate checkin, LocalDate checkOut, String customerEmail){
        this.numPeople = numPeople;
        this.checkin = checkin;
        this.checkOut = checkOut;
        this.customerEmail = customerEmail;
    }

    public Reservation toModel(Customer c){
        return new Reservation(numPeople, checkin, checkOut, 0, false, CheckInStatus.BeforeCheckIn, c);
    }


    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

}
