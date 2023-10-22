package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;

public class SpecificRoomRequestDto {

    private int number;
    private ViewType view;
    private String description;
    private Boolean openForUse;
    private String roomType;

    public SpecificRoomRequestDto(int number, ViewType view, String description, Boolean openForUse, String roomType){
        this.number = number;
        this.view = view;
        this.description = description;
        this.openForUse = openForUse;
        this.roomType = roomType;
    }

    public SpecificRoom toModel(Room room){
        return new SpecificRoom(number, view, description, openForUse, room);
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
