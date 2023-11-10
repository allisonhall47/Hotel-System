package ca.mcgill.ecse321.hotelsystem.Model;

import jakarta.persistence.*;


@Entity
public class SpecificRoom {
    @Id
    private int number;
    private ViewType view;
    private String description;
    private Boolean openForUse;

    @ManyToOne
    private Room room;

    public SpecificRoom(int number, ViewType view, String description, Boolean openForUse, Room room) {
        this.number = number;
        this.view = view;
        this.description = description;
        this.openForUse = openForUse;
        this.room = room;
    }

    public SpecificRoom() {
    }

    public int getNumber() {
        return number;
    }

    public ViewType getView() {
        return view;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getOpenForUse() {
        return openForUse;
    }

    public void setView(ViewType view) {
        this.view = view;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpenForUse(Boolean openForUse) {
        this.openForUse = openForUse;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}