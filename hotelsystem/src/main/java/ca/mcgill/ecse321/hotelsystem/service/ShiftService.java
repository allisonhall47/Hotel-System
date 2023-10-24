package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    /**
     * GetAllShifts: service method to fetch all existing shifts in the database
     * @return List of shifts
     * @throws HRSException if no shifts exist in the system
     */
    @Transactional
    public List<Shift> getAllShifts(){
        List<Shift> shifts = shiftRepository.findAll();
        if (shifts.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no shifts in the system.");
        }
        return shifts;
    }
}
