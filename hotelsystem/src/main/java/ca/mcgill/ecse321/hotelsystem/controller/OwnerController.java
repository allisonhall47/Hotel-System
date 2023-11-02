package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(origins="*")
@RestController
public class OwnerController {

    @Autowired
    private OwnerService ownerService;


    @GetMapping("/owners")
    public Iterable<OwnerResponseDto> getAllOwners() {
        return StreamSupport.stream(ownerService.getAllOwners().spliterator(), false).map(OwnerResponseDto::new).collect(Collectors.toList());
    }
}
