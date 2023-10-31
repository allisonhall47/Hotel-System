package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Customer;
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
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * GetAllCustomers: service method to fetch all existing customers in the database
     * @return List of customers
     * @throws HRSException if no customers exist in the system
     */
    @Transactional
    public List<Customer> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        if(customers.size() == 0){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no customers in the system.");
        }
        return customers;
    }

    /**
     * GetCustomerByEmail: service number to fetch an existing customer with a specific email
     * @param email: email of the customer
     * @return customer
     * @throws HRSException if the customer does not exist
     */
    @Transactional
    public Customer getCustomerByEmail(String email){
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        return customer;
    }

    /**
     * CreateCustomer: service method to create and store a customer in the database
     * @param customer: customer to be created
     * @return created customer
     * @throws HRSException if a user with the email already exist
     */
    @Transactional
    public Customer createCustomer(Customer customer){
        isValidCustomer(customer);
        if ((customerRepository.findCustomerByEmail(customer.getEmail()) == null) && (employeeRepository.findEmployeeByEmail(customer.getEmail()) == null) && (ownerRepository.findOwnerByEmail(customer.getEmail()) == null)) {
            return customerRepository.save(customer);
        } else {
            throw new HRSException(HttpStatus.CONFLICT, "A user with this email already exists.");
        }
    }

    /**
     * UpdateCustomerInformation: service method to update information in a customer
     * @param newCustomerInfo: owner with new information
     * @return updated customer
     * @throws HRSException if customer not found
     */
    @Transactional
    public Customer updateCustomerInformation(Customer newCustomerInfo){
        isValidCustomer(newCustomerInfo);
        Customer customer = getCustomerByEmail(newCustomerInfo.getEmail());
        if (customer == null){
            throw new HRSException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        customer.setName(newCustomerInfo.getName());
        customer.setAccount(newCustomerInfo.getAccount());
        return customerRepository.save(customer);
    }

    /**
     * IsValidCustomer: checks if account is valid to update or create
     * @param customer: customer to check if valid
     * @throws HRSException if customer is null or has a null field
     */
    private void isValidCustomer(Customer customer){
        if (customer == null){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty customer.");
        }
        if (customer.getName() == null || customer.getEmail() == null){
            throw new HRSException(HttpStatus.BAD_REQUEST, "Empty field in the customer.");
        }
    }

}
