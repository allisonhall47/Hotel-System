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

    @GetMapping ("/repair")
    @ResponseStatus(HttpStatus.OK)
    public List<RepairResponseDto> getAllRepairs() {
        return service.getAllRepairs().stream().map(rep -> new RepairResponseDto(rep)).toList();
    }

    @GetMapping ("/repair/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto getRepairWithId(@PathVariable int id) {
        return new RepairResponseDto(service.readRepairById(id));
    }

    @GetMapping ("repair/employee/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<RepairResponseDto> getRepairsForEmployeeEmail(@PathVariable String email) {
        return service.getRepairsByEmployeeEmail(email).stream().map(rep -> new RepairResponseDto(rep)).toList();
    }

    @PostMapping ("repair/new")
    @ResponseStatus(HttpStatus.CREATED)
    public RepairResponseDto createRepair(@RequestBody RepairRequestDto rep) {
        Repair repair = service.createRepair(rep.getEmployeeEmail(), rep.getDescription());
        return new RepairResponseDto(repair);
    }

    @PostMapping ("repair/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto changeRepairStatus(@PathVariable int id, @RequestBody CompletionStatus status) {
        return new RepairResponseDto(service.changeRepairStatus(id, status));
    }

    @PostMapping ("repair/employee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepairResponseDto changeRepairStatus(@PathVariable int id, @RequestBody String employeeEmail) {
        return new RepairResponseDto(service.changeRepairAssignedEmployee(id, employeeEmail));
    }

    @DeleteMapping("repair/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRepair(@PathVariable int id) {
        service.deleteRepair(id);
        return "redirect:/repair";
    }

}
