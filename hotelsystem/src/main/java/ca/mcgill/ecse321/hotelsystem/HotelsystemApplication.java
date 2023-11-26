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
	CommandLineRunner initDatabase(@Autowired OwnerService ownerService, @Autowired RoomService roomService, @Autowired SpecificRoomService specificRoomService, @Autowired OwnerRepository ownerRepository, @Autowired AccountService accountService, @Autowired EmployeeService employeeService, @Autowired EmployeeRepository employeeRepository, @Autowired CustomerService customerService, @Autowired CustomerRepository customerRepository, @Autowired ReservationService reservationService, @Autowired ReservedRoomService reservedRoomService){
		return args -> {
			if (ownerRepository.count() == 0){
				Account a = accountService.createAccount(new Account("OwnerAccount123", "The Marwaniott", LocalDate.of(1980, 1, 1)));
				ownerService.createOwner(new Owner("marwan@themarwaniott.com", "Marwan", a));
				SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(10, ViewType.Mountain, "description", true, roomService.createRoom(new Room("Regular", 10, BedType.Double, 2))));
				specificRoomService.createSpecificRoom(new SpecificRoom(15, ViewType.Mountain, "description", false, roomService.createRoom(new Room("Suite", 15, BedType.Double, 2))));
				Account b = accountService.createAccount(new Account("EmployeeAccount123","The Marwaniott", LocalDate.of(1980,1,1)));
				employeeService.createEmployee(new Employee("tony@themarwaniott.com", "Tony", 6000, b));
				Account c = accountService.createAccount(new Account("PasswordAccount123", "The Marwaniott", LocalDate.of(1980,1,1)));
				Customer customer = new Customer("anniegouchee@gmail.com","Annie", c);
				customerService.createCustomer(customer);
				Reservation res1 = reservationService.createReservation(new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 0, false, CheckInStatus.BeforeCheckIn, customer));
				reservedRoomService.createReservedRoom(new ReservedRoom(res1, specificRoom));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelsystemApplication.class, args);
	}
}
