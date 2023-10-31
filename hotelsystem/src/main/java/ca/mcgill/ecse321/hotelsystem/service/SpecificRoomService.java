package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.SpecificRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificRoomService {

    @Autowired
    SpecificRoomRepository specificRoomRepository;

    /**
     * GetAllSpecificRooms: service method to fetch all existing specific rooms in the database
     * @return List of specific rooms
     * @throws HRSException if no specific rooms exist in the system
     */
    @Transactional
    public List<SpecificRoom> getAllSpecificRooms(){
        List<SpecificRoom> specificRooms = specificRoomRepository.findAll();
        if (specificRooms.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no specific rooms in the system.");
        }
        return specificRooms;
    }
}
