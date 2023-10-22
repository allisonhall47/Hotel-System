package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;

public class ReservedRoomResponseDto {

    private int reservedId;
    private int linkedReservationId;
    private int roomNumber;

    public ReservedRoomResponseDto(ReservedRoom reservedRoom){
        this.reservedId = reservedRoom.getReservedID();
        if (reservedRoom.getReservation() != null) this.linkedReservationId = reservedRoom.getReservation().getReservationID();
        if (reservedRoom.getSpecificRoom() != null) this.roomNumber = reservedRoom.getSpecificRoom().getNumber();
    }

    public int getReservedId() {
        return reservedId;
    }

    public void setReservedId(int reservedId) {
        this.reservedId = reservedId;
    }

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
