package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import ca.mcgill.ecse321.hotelsystem.dto.RepairRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.RepairResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RepairController {

    @Autowired
    private RepairService service;

    @GetMapping ("/repair")
    public List<RepairResponseDto> getAllRepairs() {
        return service.getAllRepairs().stream().map(rep -> new RepairResponseDto(rep)).toList();
    }

    @GetMapping ("/repair/{id}")
    public RepairResponseDto getRepairWithId(@PathVariable int id) {
        return new RepairResponseDto(service.readRepairById(id));
    }

    @GetMapping ("repair/employee/{email}")
    public List<RepairResponseDto> getRepairsForEmployeeEmail(@PathVariable String email) {
        return service.getRepairsByEmployeeEmail(email).stream().map(rep -> new RepairResponseDto(rep)).toList();
    }

    @PostMapping ("repair/new")
    public RepairResponseDto createRepair(@RequestBody RepairRequestDto rep) {
        Repair repair = service.createRepair(rep.getEmployeeId(), rep.getDescription());
        return new RepairResponseDto(repair);
    }

    @PostMapping ("repair/status/{id}") //TODO: Completion status in requestBody? (make dto?)
    public RepairResponseDto changeRepairStatus(@PathVariable int id, @RequestBody CompletionStatus status) {
        return new RepairResponseDto(service.changeRepairStatus(id, status));
    }

    @DeleteMapping("repair/{id}")
    public String deleteRepair(@PathVariable int id) {
        service.deleteRepair(id);
        return "redirect:/repair";
    }

}
