package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.dto.RepairRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RepairResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RepairController {

    @Autowired
    private RepairService service;

    /**
     * Get all the repairs that are currently in the database
     * @return List of all repairs
     */
    @GetMapping ("/repair")
    @ResponseStatus(HttpStatus.OK)
    public List<RepairResponseDto> getAllRepairs() {
        return service.getAllRepairs().stream().map(rep -> new RepairResponseDto(rep)).toList();
    }

    /**
     * Get repair with given id if currently in database
     * @param id Id of the repair
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if no repair with given id exists in database
     * @return RepairResponseDto containing the repair data
     */
    @GetMapping ("/repair/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto getRepairWithId(@PathVariable int id) {
        return new RepairResponseDto(service.readRepairById(id));
    }

    /**
     * Get repairs for the given employee email, if the employee exists
     * @param email Email address of the employee whose assigned repairs should be returned
     * @throws  ca.mcgill.ecse321.hotelsystem.exception.HRSException if no employee with the given email exists
     * @return List of repairs assigned to the given employee
     */
    @GetMapping ("repair/employee/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<RepairResponseDto> getRepairsForEmployeeEmail(@PathVariable String email) {
        return service.getRepairsByEmployeeEmail(email).stream().map(rep -> new RepairResponseDto(rep)).toList();
    }


    /**
     * Create a new repair in the database
     * @param rep Data for the repair that should be created
     * @return Data of the newly created repair
     */
    @PostMapping ("repair/new")
    @ResponseStatus(HttpStatus.CREATED)
    public RepairResponseDto createRepair(@RequestBody RepairRequestDto rep) {
        Repair repair = service.createRepair(rep.getEmployeeEmail(), rep.getDescription());
        return new RepairResponseDto(repair);
    }

    /**
     * Change the status of a repair, throw an HRSException if the repair id or the status i
     * @param id Id of the repair whose status should be changed
     * @param status New status of the repair
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if the repair id or the status are invalid
     * @return Updated repair
     */
    @PostMapping ("repair/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto changeRepairStatus(@PathVariable int id, @RequestBody CompletionStatus status) {
        return new RepairResponseDto(service.changeRepairStatus(id, status));
    }

    /**
     * Change the assigned employee of a repair
     * @param id Id of the repair whose assigned employee should be changed
     * @param employeeEmail Email of the newly assigned employee
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if the repair id or the employee email are invalid
     * @return Updated repair
     */
    @PostMapping ("repair/employee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto changeRepairEmployee(@PathVariable int id, @RequestBody String employeeEmail) {
        return new RepairResponseDto(service.changeRepairAssignedEmployee(id, employeeEmail));
    }

    /**
     * Delete repair with the given id
     * @param id Id of the repair to delete
     * @throws ca.mcgill.ecse321.hotelsystem.exception.HRSException if repair with given id does not exist
     * @return Redirection cmd to list of all repairs
     */
    @DeleteMapping("repair/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRepair(@PathVariable int id) {
        service.deleteRepair(id);
        return "redirect:/repair";
    }

}
