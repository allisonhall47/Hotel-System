package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findCustomerByEmail(String email);
    void deleteCustomerBy(String email);
    Customer findCustomerByName(String name);
    Customer findCustomerByAccount_AccountNumber(int accountNumber);
    void deleteCustomerById(String s);
    List<Customer> findAll();
}
