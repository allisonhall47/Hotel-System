package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

public class RequestRequestDto {

    private int requestId;
    private String description;
    private int room;
    private int reservationId;
    private CompletionStatus status;

    public RequestRequestDto(int requestId, String description, int room, int reservationId, CompletionStatus status){
        this.requestId = requestId;
        this.description = description;
        this.room = room;
        this.reservationId = reservationId;
        this.status = status;
    }

    public Request toModel(Reservation r){
        return new Request(status, description, r);
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public void setStatus(CompletionStatus status) {
        this.status = status;
    }
}
