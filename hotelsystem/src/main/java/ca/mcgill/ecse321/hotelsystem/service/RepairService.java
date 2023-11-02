package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.RepairRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairService {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Repair createRepair(int employeeAccountId, String description) {
        if (description == null || description.length() == 0) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid repair description (empty)");
        }

        Employee emp = employeeRepository.findEmployeeByAccount_AccountNumber(employeeAccountId);
        if (emp == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No employee with account id %d", employeeAccountId));
        }
        return repairRepository.save(new Repair(CompletionStatus.Pending, description, emp));
    }

    @Transactional
    public Repair changeRepairStatus(int id, CompletionStatus status) {
        if (status == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid repair status (null)");
        }
        Repair rep = repairRepository.findRepairByRepairId(id);
        if (rep == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No repair with id %d", id));
        }

        rep.setStatus(status);
        repairRepository.save(rep);

        return rep;
    }

    @Transactional
    public void deleteRepair(int id) {
        Repair rep = repairRepository.findRepairByRepairId(id);
        if (rep == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No repair with id %d", id));
        }
        repairRepository.deleteRepairByRepairId(id);
    }

    @Transactional
    public List<Repair> getRepairsByEmployeeEmail(String email) {
        return repairRepository.findRepairsByEmployee_Email(email);
    }



    @Transactional
    public Repair readRepairById(int id) {
        Repair repair = repairRepository.findRepairByRepairId(id);
        if (repair == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No repair with id %d", id));
        }
        return repair;
    }

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
