package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

public class RequestResponseDto {
    private int requestId;
    private String description;
    private Reservation reservation;
    private CompletionStatus status;

    public RequestResponseDto(Request request){
        this.requestId = request.getRequestId();
        this.description = request.getDescription();
        this.reservation = request.getReservation();
        this.status = request.getStatus();
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public void setStatus(CompletionStatus status) {
        this.status = status;
    }

}
