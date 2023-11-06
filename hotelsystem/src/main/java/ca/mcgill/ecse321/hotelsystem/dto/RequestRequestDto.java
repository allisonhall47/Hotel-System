package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;

public class RequestRequestDto {

    private String description;
    private int reservationId;

    public RequestRequestDto(String description, int reservationId){
        this.description = description;
        this.reservationId = reservationId;
    }

    public RequestRequestDto() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

}
