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
}
