package ca.mcgill.ecse321.hotelsystem.dto;

import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.Model.Room;

public class RoomResponseDto {
    private String type;
    private int rate;
    private BedType bedType;
    private int capacity;

    public RoomResponseDto(Room room){
        this.type = room.getType();
        this.rate = room.getRate();
        this.bedType = room.getBedType();
        this.capacity = room.getCapacity();
    }

    public RoomResponseDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
