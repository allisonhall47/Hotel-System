package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    /**
     * GetAllRooms: service method to fetch all existing rooms in the database
     * @return List of rooms
     * @throws HRSException if no rooms exist in the system
     */
    @Transactional
    public List<Room> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        if (rooms.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no rooms in the system.");
        }
        return rooms;
    }

    @Transactional
    public Room createRoom(Room room){
        isValidRoom(room);
        room = roomRepository.save(room);
        return room;
    }

    @Transactional
    public Room updateRoom(Room room){
        isValidRoom(room);
        Room oldRoom = getRoomByType(room.getType());
        if (oldRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "No rooms in the system with type " + room.getType() + ".");
        }
        oldRoom.setRate(room.getRate());
        oldRoom.setCapacity(room.getCapacity());
        oldRoom.setBedType(room.getBedType());
        return roomRepository.save(oldRoom);
    }

    @Transactional
    public Room getRoomByType(String type){
        if(type == null || type.isEmpty()){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Please enter a valid string.");
        }
        Room room = roomRepository.findRoomByType(type);
        if(room ==  null){
            throw new HRSException(HttpStatus.NOT_FOUND, "No rooms in the system with type "+ type + ".");
        }
        return room;
    }

    public void isValidRoom(Room room){
        if(!room.getType().equals("Suite") && !room.getType().equals("Deluxe") && !room.getType().equals("Luxury") && !room.getType().equals("Regular")){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid room type.");
        }
        if(room.getRate()<=0){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid room rate.");
        }
        if(room.getBedType()==null){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid bed type.");
        }
        if(room.getCapacity()<=0){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid room capacity.");
        }
    }
}
