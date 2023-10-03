package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;


@Entity
public class ReservedRoom {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservationID")
    private Reservation reservation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "number")
    private SpecificRoom specificRoom;

    public ReservedRoom(Reservation reservation, SpecificRoom specificRoom) {
        this.reservation = reservation;
        this.specificRoom = specificRoom;
    }

    public SpecificRoom getSpecificRoom() {
        return specificRoom;
    }

    public void setSpecificRoom(SpecificRoom specificRoom) {
        this.specificRoom = specificRoom;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}