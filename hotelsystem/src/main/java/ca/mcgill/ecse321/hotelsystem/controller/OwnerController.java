package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Owner;
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
    public ResponseEntity<OwnerResponseDto> createOwner(@RequestBody OwnerRequestDto ownerRequest) {
        Owner owner = ownerRequest.getAccountNumber() == 0 ?
                ownerRequest.toModel(null) :
                ownerRequest.toModel(accountService.getAccountByAccountNumber(ownerRequest.getAccountNumber()));
        owner = ownerService.createOwner(owner);
        return new ResponseEntity<>(new OwnerResponseDto(owner), HttpStatus.CREATED);
    }

    /**
     * Retrieve an owner by their email address.
     *
     * @param email The email address of the owner to retrieve.
     * @return A ResponseEntity containing the OwnerResponseDto if found, otherwise HttpStatus.NOT_FOUND.
     */
    @GetMapping(value = {"/email", "/email/"})
    public ResponseEntity<OwnerResponseDto> getOwnerByEmail(@RequestParam String email) {
        Owner owner = ownerService.getOwnerByEmail(email);
        return owner != null ?
                new ResponseEntity<>(new OwnerResponseDto(owner), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Update an existing owner's information.
     *
     * @param ownerRequest The request DTO containing the updated information for the owner.
     * @return A ResponseEntity containing the updated OwnerResponseDto if the update is successful, otherwise HttpStatus.NOT_FOUND.
     */
    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<?> updateOwner(@RequestBody OwnerRequestDto ownerRequest) {
        // Validate the request before proceeding
        if (ownerRequest.getEmail() == null || ownerRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Empty field in the owner.");
        }

        try {
            Owner currentOwner = ownerService.getOwnerByEmail(ownerRequest.getEmail());
            Owner updatedOwner = ownerService.updateOwnerInformation(ownerRequest.toModel(currentOwner.getAccount()));
            return new ResponseEntity<>(new OwnerResponseDto(updatedOwner), HttpStatus.OK);
        } catch (HRSException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        }
    }

}
