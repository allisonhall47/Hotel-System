package ca.mcgill.ecse321.hotelsystem.service;


import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class OwnerServiceTests {


    @Mock
    private OwnerRepository ownerRepository;


    @Mock
    private EmployeeRepository employeeRepository;


    @Mock
    private CustomerRepository customerRepository;


    @InjectMocks
    private OwnerService ownerService;




    /**
     * Verifies that the getAllOwners method can successfully retrieve
     * a list of all owners when they exist in the system.
     */
    @Test
    public void testGetAllOwners() {
        Owner o1 = new Owner("john@email.com", "John Doe", null);
        List<Owner> owners = new ArrayList<>();
        owners.add(o1);


        when(ownerRepository.findAll()).thenReturn(owners);


        List<Owner> output = ownerService.getAllOwners();
        assertEquals(1, output.size());
        assertTrue(output.contains(o1));
    }


    /**
     * Checks if the getAllOwners method throws an HRSException
     * when there are no owners in the system.
     */
    @Test
    public void testGetAllOwners_NoneExist() {
        when(ownerRepository.findAll()).thenReturn(new ArrayList<>());


        HRSException e = assertThrows(HRSException.class, () -> ownerService.getAllOwners());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no owners in the system.");
    }


    /**
     * Ensures that the getOwnerByEmail method can fetch an owner
     * based on a provided email if it corresponds to an existing owner.
     */
    @Test
    public void testGetOwnerByEmail_Valid() {
        Owner o = new Owner("john@email.com", "John Doe", null);
        when(ownerRepository.findOwnerByEmail("john@email.com")).thenReturn(o);


        Owner output = ownerService.getOwnerByEmail("john@email.com");
        assertEquals(output, o);
    }


    /**
     * Ensures the getOwnerByEmail method throws an HRSException
     * if the provided email doesn't match any owner in the system.
     */
    @Test
    public void testGetOwnerByEmail_NotFound() {
        when(ownerRepository.findOwnerByEmail("john@email.com")).thenReturn(null);


        HRSException e = assertThrows(HRSException.class, () -> ownerService.getOwnerByEmail("john@email.com"));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Owner not found.");
    }


    /**
     * Verifies the createOwner method can successfully create an owner
     * when no owner exists and the provided email is unique across users.
     */
    @Test
    public void testCreateOwner_Valid() {
        Owner existingOwner = new Owner("existing@email.com", "Existing Owner", null); // An existing owner
        List<Owner> existingOwners = Arrays.asList(existingOwner); // List containing existing owner


        Owner o = new Owner("john@email.com", "John Doe", null); // New owner to be created


        when(ownerRepository.findAll()).thenReturn(existingOwners); // Mock existing owner in the system
        when(ownerRepository.findOwnerByEmail(o.getEmail())).thenReturn(null);
        when(employeeRepository.findEmployeeByEmail(o.getEmail())).thenReturn(null);
        when(customerRepository.findCustomerByEmail(o.getEmail())).thenReturn(null);


// Since an owner already exists in the system, the createOwner method should throw the HRSException
        Exception exception = assertThrows(HRSException.class, () -> {
            ownerService.createOwner(o);
        });


        assertEquals("An owner already exists in the system.", exception.getMessage());
    }




    /**
     * Checks if the createOwner method throws an HRSException when
     * trying to create an owner but an owner already exists in the system.
     */
    @Test
    public void testCreateOwner_AlreadyExists() {
        Owner o = new Owner("john@email.com", "John Doe", null);
        when(ownerRepository.findAll()).thenReturn(List.of(o));


        HRSException e = assertThrows(HRSException.class, () -> ownerService.createOwner(o));
        assertEquals(e.getStatus(), HttpStatus.CONFLICT);
        assertEquals(e.getMessage(), "An owner already exists in the system.");
    }




    /**
     * Verifies the updateOwnerInformation method can correctly update
     * the existing owner's details based on the new provided info.
     */
    @Test
    public void testUpdateOwnerInformation_Valid() {
        Owner oldInfo = new Owner("john@email.com", "John Doe", null);
        Owner newInfo = new Owner("john@email.com", "Johnathan Doe", null);
        when(ownerRepository.findOwnerByEmail("john@email.com")).thenReturn(oldInfo);
        when(ownerRepository.save(oldInfo)).thenReturn(newInfo);


        Owner updated = ownerService.updateOwnerInformation(newInfo);
        assertEquals(updated.getName(), "Johnathan Doe");
    }


    /**
     * Ensures the updateOwnerInformation method throws an HRSException
     * when trying to update a non-existent owner's information.
     */
    @Test
    public void testUpdateOwnerInformation_NotFound() {
        Owner newInfo = new Owner("john@email.com", "Johnathan Doe", null);
        when(ownerRepository.findOwnerByEmail("john@email.com")).thenReturn(null);


        HRSException e = assertThrows(HRSException.class, () -> ownerService.updateOwnerInformation(newInfo));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Owner not found.");
    }




    /**
     * This test verifies that the isValidOwner method throws an HRSException
     * when an owner with an invalid email format is passed.
     */
    @Test
    public void testIsValidOwner_InvalidEmail() {
        String invalidEmail = "john.doe.com"; // no '@' symbol, so it's invalid
        Owner ownerWithInvalidEmail = new Owner(invalidEmail, "John Doe", null);


        HRSException e = assertThrows(HRSException.class,
                () -> ownerService.createOwner(ownerWithInvalidEmail));


        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("Invalid email address.", e.getMessage());
    }
}
