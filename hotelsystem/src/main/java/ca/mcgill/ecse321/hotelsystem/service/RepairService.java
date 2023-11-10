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

    /**
     * Create new repair in databse
     * @param employeeEmail Email of the assigned employee
     * @param description Description of what needs to be done
     * @throws HRSException if no employee with the given email exists or if the description is invalid (<10 characters)
     * @return Newly created repair
     */
    @Transactional
    public Repair createRepair(String employeeEmail, String description) {
        if (description == null || description.isBlank()) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid repair description: Empty");
        } else if (description.length() < 10) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid repair description: Too short");
        }

        Employee emp = isValidEmployee(employeeEmail);
        return repairRepository.save(new Repair(CompletionStatus.Pending, description, emp));
    }

    /**
     * Change the status of a repair
     * @param id Id of the repair whose status should be changed
     * @param status New status of the repair
     * @throws HRSException if no repair with the given id exists or if the status is null
     * @return Updated repair
     */
    @Transactional
    public Repair changeRepairStatus(int id, CompletionStatus status) {
        if (status == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Invalid repair status (null)");
        }
        Repair rep = isValidRepair(id);

        rep.setStatus(status);
        rep = repairRepository.save(rep);

        return rep;
    }

    /**
     * Changed the assigned employee of a reservation
     * @param id Id of the repair whose employee should be changed
     * @param email Email of the newly assigned employee
     * @throws HRSException if no repair with the given id or no employee with the given email exists
     * @return Updated repair
     */
    @Transactional
    public Repair changeRepairAssignedEmployee(int id, String email) {
        isValidEmployee(email);
        Repair rep = isValidRepair(id);
        Employee newEmp = isValidEmployee(email);
        rep.setEmployee(newEmp);
        repairRepository.save(rep);
        return rep;
    }

    /**
     * Delete repair with the given id
     * @throws HRSException if no repair with the given id exists
     * @param id Id of the repair to delete
     */
    @Transactional
    public void deleteRepair(int id) {
        isValidRepair(id);
        repairRepository.deleteRepairByRepairId(id);
    }

    /**
     * Get all repairs assigned to the given employee
     * @param email Email of the employee whose assigned tasks should be returned
     * @throws HRSException if no employee with the given email exists
     * @return List of repairs assigned to the employee email
     */
    @Transactional
    public List<Repair> getRepairsByEmployeeEmail(String email) {
        isValidEmployee(email);
        return repairRepository.findRepairsByEmployee_Email(email);
    }

    /**
     * Get the repair with the given id
     * @param id Id of the repair
     * @throws HRSException if no repair with the given id exists
     * @return the repair with the given id
     */
    @Transactional
    public Repair readRepairById(int id) {
        return isValidRepair(id);
    }

    /**
     * Get all the repairs that are currently in the database
     * @return List of repairs
     */
    @Transactional
    public List<Repair> getAllRepairs(){
        List<Repair> repairs = repairRepository.findAll();
        return repairs;
    }

    private Employee isValidEmployee(String email) {
        if (email == null) {
            throw new HRSException(HttpStatus.BAD_REQUEST, "Email can't be null");
        }
        Employee emp = employeeRepository.findEmployeeByEmail(email);
        if (emp == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "No employee with email " + email);
        }
        return emp;
    }

    private Repair isValidRepair(int id) {
        Repair repair = repairRepository.findRepairByRepairId(id);
        if (repair == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, String.format("No repair with id %d", id));
        }
        return repair;
    }
}
