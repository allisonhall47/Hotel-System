package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;


@Entity
public class Room {
    @Id
    private String type;
    private int rate;
    private BedType bedType;
    private int capacity;

    public Room(String type, int rate, BedType bedType, int capacity) {
        this.type = type;
        this.rate = rate;
        this.bedType = bedType;
        this.capacity = capacity;
    }

    public Room() {
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


