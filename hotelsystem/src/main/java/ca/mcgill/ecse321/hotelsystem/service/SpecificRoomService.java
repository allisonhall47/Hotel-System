package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
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

    /**
     * findSpecificRoomByNumber: service method to fetch specific room in the database by number
     * @param number: room number
     * @return a specific room
     * @throws HRSException if specific room does not exist in the system or the number is invalid
     */
    @Transactional
    public SpecificRoom findSpecificRoomByNumber(int number){
        if(number < 0){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid number.");
        }
        SpecificRoom specificRoom = specificRoomRepository.findSpecificRoomByNumber(number);
        if(specificRoom == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "There is no specific room in the system with number "+ number + ".");
        }
        return specificRoom;
    }

    /**
     * findSpecificRoomsByRoomType: service method to fetch all existing specific rooms in the database by type
     * @param type: room type
     * @return List of specific rooms
     * @throws HRSException if no specific rooms with type exist in the system or type is invalid
     */
    @Transactional
    public List<SpecificRoom> findSpecificRoomsByRoomType(String type){
        if(!type.equals("Suite") && !type.equals("Deluxe") && !type.equals("Luxury") && !type.equals("Regular")){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid room type.");
        }
        List<SpecificRoom> specificRooms = specificRoomRepository.findSpecificRoomsByRoom_Type(type);
        if(specificRooms == null || specificRooms.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no specific rooms in the system with type " + type + ".");
        }
        return specificRooms;
    }

    /**
     * findSpecificRoomsByView: service method to fetch all existing specific rooms in the database by view
     * @param view: room view
     * @return List of specific rooms
     * @throws HRSException if no specific rooms with view exist in the system
     */
    @Transactional
    public List<SpecificRoom> findSpecificRoomsByView(ViewType view){
        List<SpecificRoom> specificRooms = specificRoomRepository.findSpecificRoomsByView(view);
        if(specificRooms == null || specificRooms.size()==0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no specific rooms in the system with this view type.");
        }
        return specificRooms;
    }

    /**
     * findSpecificRoomsByView: service method to fetch all existing specific rooms in the database that are open for use
     * @return List of specific rooms
     * @throws HRSException if no specific rooms open for use exist in the system
     */
    @Transactional
    public List<SpecificRoom> findSpecificRoomsOpenForUse(){
        List<SpecificRoom> specificRooms = specificRoomRepository.findSpecificRoomsByOpenForUseIsTrue();
        if(specificRooms == null || specificRooms.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no specific rooms open for use.");
        }
        return specificRooms;
    }

    /**
     * findSpecificRoomsByView: service method to delete the specific room in the database with number
     * @param number: room number
     * @throws HRSException if no specific room with number is found or number is invalid
     */
    @Transactional
    public void deleteSpecificRoomByNumber(int number){
        if(number < 0){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid number.");
        }
        SpecificRoom specificRoom = specificRoomRepository.findSpecificRoomByNumber(number);
        if(specificRoom == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "There is no specific room in the system with number "+ number + ".");
        }
        specificRoomRepository.deleteByNumber(number);
    }

    /**
     * createRoom: service method that creates a specific room and adds it to the database
     * @param specificRoom: new specific room
     * @return room
     * @throws HRSException if the specific room is invalid
     */
    @Transactional
    public SpecificRoom createSpecificRoom(SpecificRoom specificRoom){
        if(specificRoom.getNumber()<0){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid number.");
        }
        specificRoom = specificRoomRepository.save(specificRoom);
        return specificRoom;
    }

    /**
     * createRoom: service method that updates a specific room and adds it to the database
     * @param specificRoom: new specific room details
     * @return room
     * @throws HRSException if the specific room is invalid or no specific room is found
     */
    @Transactional
    public SpecificRoom updateSpecificRoom(SpecificRoom specificRoom){
        SpecificRoom oldSpecificRoom = findSpecificRoomByNumber(specificRoom.getNumber());
        if (oldSpecificRoom == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "No specific rooms in the system with number "+ specificRoom.getNumber() + ".");
        }
        oldSpecificRoom.setRoom(specificRoom.getRoom());
        oldSpecificRoom.setDescription(specificRoom.getDescription());
        oldSpecificRoom.setView(specificRoom.getView());
        oldSpecificRoom.setOpenForUse(specificRoom.getOpenForUse());
        return specificRoomRepository.save(oldSpecificRoom);
    }
}
