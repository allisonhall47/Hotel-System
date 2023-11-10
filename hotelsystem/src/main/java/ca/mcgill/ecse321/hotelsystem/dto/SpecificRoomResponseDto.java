package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;

public class SpecificRoomResponseDto {
    private int number;
    private ViewType view;
    private String description;
    private Boolean openForUse;
    private String roomType;

    public SpecificRoomResponseDto(SpecificRoom specificRoom){
        this.number = specificRoom.getNumber();
        this.view = specificRoom.getView();
        this.description = specificRoom.getDescription();
        this.openForUse = specificRoom.getOpenForUse();
        if (specificRoom.getRoom() != null) this.roomType = specificRoom.getRoom().getType();
    }

    public SpecificRoomResponseDto() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ViewType getView() {
        return view;
    }

    public void setView(ViewType view) {
        this.view = view;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getOpenForUse() {
        return openForUse;
    }

    public void setOpenForUse(Boolean openForUse) {
        this.openForUse = openForUse;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

}
