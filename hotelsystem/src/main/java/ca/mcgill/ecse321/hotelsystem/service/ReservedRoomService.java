package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservedRoomService {

    @Autowired
    ReservedRoomRepository reservedRoomRepository;

    /**
     * GetAllReservedRooms: service method to fetch all existing reserved rooms in the database
     * @return List of reserved rooms
     * @throws HRSException if no reserved rooms exist in the system
     */
    @Transactional
    public List<ReservedRoom> getAllReservedRooms(){
        List<ReservedRoom> reservedRooms = reservedRoomRepository.findAll();
        if (reservedRooms.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no reserved rooms in the system.");
        }
        return reservedRooms;
    }
}
