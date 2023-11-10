package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationID;
    private int numPeople;
    private LocalDate checkin;
    private LocalDate checkOut;
    private int totalPrice;
    private boolean paid;
    private CheckInStatus checkedIn;

    @ManyToOne
    private Customer customer;


    public Reservation( int numPeople, LocalDate checkin, LocalDate checkOut, int totalPrice, boolean paid, CheckInStatus checkedIn, Customer customer) {
        this.numPeople = numPeople;
        this.checkin = checkin;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.checkedIn = checkedIn;
        this.customer = customer;
    }

    public Reservation() {
    }

    public int getReservationID() {
        return reservationID;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public LocalDate getCheckIn() {
        return checkin;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkin = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public CheckInStatus getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(CheckInStatus checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}