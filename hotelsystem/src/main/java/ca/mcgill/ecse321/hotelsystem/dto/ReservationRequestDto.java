package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

import java.sql.Date;


public class ReservationRequestDto {

    private int reservationId;
    private int numPeople;
    private Date checkin;
    private Date checkOut;
    private int totalPrice;
    private Boolean paid;
    private CheckInStatus checkedIn;

    private String customerEmail;

    public ReservationRequestDto(int reservationId, int numPeople, Date checkin, Date checkOut, int totalPrice, Boolean paid, CheckInStatus checkedIn, String customerEmail){
        this.reservationId = reservationId;
        this.numPeople = numPeople;
        this.checkin = checkin;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.checkedIn = checkedIn;
        this.customerEmail = customerEmail;
    }

    public Reservation toModel(Customer c){
        return new Reservation(numPeople, checkin, checkOut, totalPrice, paid, checkedIn, c);
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public CheckInStatus getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(CheckInStatus checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
