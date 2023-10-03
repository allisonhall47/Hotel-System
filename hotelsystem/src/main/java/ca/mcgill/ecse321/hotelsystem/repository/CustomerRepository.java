package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findCustomerByEmail(String email);
    void deleteCustomerByEmail(String email);
    List<Customer> findCustomersByName(String name);
    Customer findCustomerByAccount_AccountNumber(int accountNumber);
    void deleteCustomerByName(String s);
    List<Customer> findAll();
}
