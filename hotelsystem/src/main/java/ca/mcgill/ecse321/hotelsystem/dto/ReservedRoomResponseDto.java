package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;

public class ReservedRoomResponseDto {

    private int reservedId;
    private Reservation reservation;
    private SpecificRoom room;

    public ReservedRoomResponseDto() {}

    public ReservedRoomResponseDto(ReservedRoom reservedRoom){
        this.reservedId = reservedRoom.getReservedID();
        this.room = reservedRoom.getSpecificRoom();
        this.reservation = reservedRoom.getReservation();
    }

    public int getReservedId() {
        return reservedId;
    }

    public void setReservedId(int reservedId) {
        this.reservedId = reservedId;
    }


    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public SpecificRoom getRoom() {
        return room;
    }

    public void setRoom(SpecificRoom room) {
        this.room = room;
    }
}
