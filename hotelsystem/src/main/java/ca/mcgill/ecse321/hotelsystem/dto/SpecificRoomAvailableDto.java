package ca.mcgill.ecse321.hotelsystem.dto;

import java.time.LocalDate;

public class SpecificRoomAvailableDto {
    private LocalDate checkin;
    private LocalDate checkOut;


    public SpecificRoomAvailableDto() {

    }

    public SpecificRoomAvailableDto(LocalDate checkin, LocalDate checkOut) {
        this.checkin = checkin;
        this.checkOut = checkOut;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
