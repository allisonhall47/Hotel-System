package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairService {

    @Autowired
    RepairRepository repairRepository;

    /**
     * GetAllRepairs: service method to fetch all existing repairs in the database
     * @return List of repairs
     * @throws HRSException if no repairs exist in the system
     */
    @Transactional
    public List<Repair> getAllRepairs(){
        List<Repair> repairs = repairRepository.findAll();
        if (repairs.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no repairs in the system.");
        }
        return repairs;
    }
}
