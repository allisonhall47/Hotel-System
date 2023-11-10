package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.dto.OwnerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.OwnerResponseDto;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.service.AccountService;
import ca.mcgill.ecse321.hotelsystem.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/owner") // base path for all owner-related endpoints
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AccountService accountService;

    /**
     * Retrieve all owners in the system.
     *
     * @return A list of OwnerResponseDto objects representing all owners.
     */
    @GetMapping(value = {"/", ""})
    public List<OwnerResponseDto> getAllOwners() {
        return ownerService.getAllOwners().stream().map(OwnerResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Create a new owner in the system.
     *
     * @param ownerRequest The request DTO containing the owner's information.
     * @return A ResponseEntity containing the newly created OwnerResponseDto and HttpStatus.CREATED.
     */
    @PostMapping(value = {"/create", "/create/"})
    public ResponseEntity<OwnerResponseDto> createOwner(@RequestBody OwnerRequestDto ownerRequest){
        Owner owner;
        if(ownerRequest.getAccountNumber() == 0){
            owner = ownerRequest.toModel(null);
        } else {
            owner = ownerRequest.toModel(accountService.getAccountByAccountNumber(ownerRequest.getAccountNumber()));
        }
        owner = ownerService.createOwner(owner);
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(owner), HttpStatus.CREATED);
    }

    /**
     * Retrieve an owner by their email address.
     *
     * @param email The email address of the owner to retrieve.
     * @return A ResponseEntity containing the OwnerResponseDto if found, otherwise HttpStatus.NOT_FOUND.
     */
    @GetMapping(value = {"/email", "/email/"})
    public ResponseEntity<OwnerResponseDto> getOwnerByEmail(@RequestParam String email) {
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(ownerService.getOwnerByEmail(email)), HttpStatus.OK);
    }

    /**
     * Update an existing owner's information.
     *
     * @param ownerRequest The request DTO containing the updated information for the owner.
     * @return A ResponseEntity containing the updated OwnerResponseDto if the update is successful, otherwise HttpStatus.NOT_FOUND.
     */
    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<?> updateOwner(@RequestBody OwnerRequestDto ownerRequest) {
        Owner owner;
        if(ownerRequest.getAccountNumber() == 0){
            owner = ownerRequest.toModel(null);
        } else {
            owner = ownerRequest.toModel(accountService.getAccountByAccountNumber(ownerRequest.getAccountNumber()));
        }
        owner = ownerService.updateOwnerInformation(owner);
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(owner), HttpStatus.OK);
    }

}
