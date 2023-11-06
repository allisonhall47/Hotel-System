package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;

public class ReservedRoomRequestDto {

    //private int reservedId;
    private int linkedReservationId;
    private int roomNumber;

    public ReservedRoomRequestDto() {

    }

    public ReservedRoomRequestDto(int linkedReservationId, int roomNumber){
        //this.reservedId = reservedId;
        this.linkedReservationId = linkedReservationId;
        this.roomNumber = roomNumber;
    }

    public ReservedRoom toModel(Reservation r, SpecificRoom s){
        return new ReservedRoom(r, s);
    }

//    public int getReservedId() {
//        return reservedId;
//    }
//
//    public void setReservedId(int reservedId) {
//        this.reservedId = reservedId;
//    }

    public int getLinkedReservationId() {
        return linkedReservationId;
    }

    public void setLinkedReservationId(int linkedReservationId) {
        this.linkedReservationId = linkedReservationId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

}
