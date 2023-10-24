package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    /**
     * GetAllOwners: service method to fetch all existing owners in the database
     * @return List of owners
     * @throws HRSException if no owners exist in the system
     */
    @Transactional
    public List<Owner> getAllOwners(){
        List<Owner> owners = ownerRepository.findAll();
        if (owners.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no owners in the system.");
        }
        return owners;
    }

    /**
     * GetOwnerByEmail: service number to fetch an existing owner with a specific email
     * @param email: email of the owner
     * @return owner
     * @throws HRSException if the owner does not exist
     */
    @Transactional
    public Owner getOwnerByEmail(String email){
        Owner owner = ownerRepository.findOwnerByEmail(email);
        if (owner == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Owner not found.");
        }
        return owner;
    }

    /**
     * CreateOwner: service method to create and store an owner in the database
     * @param email: email for the owner
     * @param name: name for the owner
     * @param account: account for the owner
     * @return created owner
     * @throws HRSException if a user with the email already exist
     */
    @Transactional
    public Owner createOwner(String email, String name, Account account){
        if ((ownerRepository.findOwnerByEmail(email) == null) && (employeeRepository.findEmployeeByEmail(email) == null) && (customerRepository.findCustomerByEmail(email) == null)) {
            Owner owner = new Owner(email, name, account);
            ownerRepository.save(owner);
            return owner;
        } else {
            throw new HRSException(HttpStatus.CONFLICT, "A user with this email already exists.");
        }
    }

    /**
     * UpdateOwnerInformation: service method to update information in an owner
     * @param newOwnerInfo: owner with new information
     * @return updated owner
     */
    @Transactional
    public Owner updateOwnerInformation(Owner newOwnerInfo){
        Owner oldOwner = getOwnerByEmail(newOwnerInfo.getEmail());
        oldOwner.setName(newOwnerInfo.getName());
        oldOwner.setAccount(newOwnerInfo.getAccount());
        return ownerRepository.save(oldOwner);
    }

}
