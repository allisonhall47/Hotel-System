package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationID;
    private int numPeople;
    private Date checkIn;
    private Date checkOut;
    private int totalPrice;
    private boolean paid;
    private CheckInStatus checkedIn;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email")
    private Customer customer;

    protected Reservation() {
    }

    public Reservation(int numPeople, Date checkIn, Date checkOut, int totalPrice, boolean paid, CheckInStatus checkedIn) {
        this.numPeople = numPeople;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.checkedIn = checkedIn;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
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