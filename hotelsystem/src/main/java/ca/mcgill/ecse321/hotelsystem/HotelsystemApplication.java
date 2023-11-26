package ca.mcgill.ecse321.hotelsystem;

import ca.mcgill.ecse321.hotelsystem.Model.*;

import ca.mcgill.ecse321.hotelsystem.repository.CustomerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringBootApplication
public class HotelsystemApplication {

	// created mock owner and employee for login
	@Bean
	CommandLineRunner initDatabase(@Autowired OwnerService ownerService, @Autowired RoomService roomService, @Autowired SpecificRoomService specificRoomService, @Autowired OwnerRepository ownerRepository, @Autowired AccountService accountService, @Autowired EmployeeService employeeService, @Autowired EmployeeRepository employeeRepository, @Autowired CustomerService customerService, @Autowired CustomerRepository customerRepository){
		return args -> {
			if (ownerRepository.count() == 0){
				Account a = accountService.createAccount(new Account("OwnerAccount123", "The Marwaniott", LocalDate.of(1980, 1, 1)));
				ownerService.createOwner(new Owner("marwan@themarwaniott.com", "Marwan", a));
				Account b = accountService.createAccount(new Account("EmployeeAccount123","The Marwaniott", LocalDate.of(1980,1,1)));
				employeeService.createEmployee(new Employee("tony@themarwaniott.com", "Tony", 6000, b));
				Account c = accountService.createAccount(new Account("PasswordAccount123", "The Marwaniott", LocalDate.of(1980,1,1)));
				customerService.createCustomer(new Customer("anniegouchee@gmail.com","Annie", c));
				Account d = accountService.createAccount(new Account("EmployeeAccount123", "The Marwaniott", LocalDate.of(1990,2,2)));
				employeeService.createEmployee(new Employee("louis@themarwaniott.com","Louis", 8000, d));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelsystemApplication.class, args);
	}
}
