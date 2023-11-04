package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.CustomerResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.AccountService;
import ca.mcgill.ecse321.hotelsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins="*")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    /**
     * GetAllCustomers: get a list of all customers in the system
     * @return list of customer response dtos
     */
    @GetMapping(value = {"/customers", "/customers/"})
    public List<CustomerResponseDto> getAllCustomers(){
        return customerService.getAllCustomers().stream().map(CustomerResponseDto::new).collect(Collectors.toList());
    }

    /**
     * CreateCustomer: create a customer
     * @param customerRequest: information for the new customer
     * @return response entity containing the response dto object
     */
    @PostMapping(value = { "/customer/create", "/customer/create/"})
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequest){
        Customer customer;
        if(customerRequest.getAccountNumber() == 0){
            customer = customerRequest.toModel(null);
        } else {
            customer = customerRequest.toModel(accountService.getAccountByAccountNumber(customerRequest.getAccountNumber()));
        }
        customer = customerService.createCustomer(customer);
        return new ResponseEntity<CustomerResponseDto>(new CustomerResponseDto(customer), HttpStatus.CREATED);
    }

    /**
     * UpdateCustomer: update the information in a pre-existing customer
     * @param customerRequest: information to update the customer with
     * @return response entity containing the response dto object
     */
    @PutMapping(value = { "/customer/update", "/customer/update/"})
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto customerRequest){
        Customer customer;
        if(customerRequest.getAccountNumber() == 0){
            customer = customerRequest.toModel(null);
        } else {
            customer = customerRequest.toModel(accountService.getAccountByAccountNumber(customerRequest.getAccountNumber()));
        }
        customer = customerService.updateCustomerInformation(customer);
        return new ResponseEntity<CustomerResponseDto>(new CustomerResponseDto(customer), HttpStatus.OK);
    }

    /**
     * GetCustomerByEmail: retrieve a customer in the system by email
     * @param email: email of the customer to retrieve
     * @return response entity containing the retrieved customer
     */
    @GetMapping(value = {"/customer", "/customer/"})
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@RequestParam String email){
        return new ResponseEntity<CustomerResponseDto>(new CustomerResponseDto(customerService.getCustomerByEmail(email)), HttpStatus.OK);
    }

    /**
     * DeleteCustomer: delete a customer from the system
     * @param email: email of the customer to delete
     */
    @DeleteMapping(value = {"/customer/delete/{email}"})
    public void deleteCustomer(@PathVariable String email){
        customerService.deleteCustomer(email);
    }
}
