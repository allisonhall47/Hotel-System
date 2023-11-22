package ca.mcgill.ecse321.hotelsystem;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Owner;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
import ca.mcgill.ecse321.hotelsystem.service.AccountService;
import ca.mcgill.ecse321.hotelsystem.service.OwnerService;
import ca.mcgill.ecse321.hotelsystem.service.RoomService;
import ca.mcgill.ecse321.hotelsystem.service.SpecificRoomService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringBootApplication
public class HotelsystemApplication {

	@Bean
	CommandLineRunner initDatabase(@Autowired OwnerService ownerService, @Autowired RoomService roomService, @Autowired SpecificRoomService specificRoomService, @Autowired OwnerRepository ownerRepository, @Autowired AccountService accountService){
		return args -> {
			if (ownerRepository.count() == 0){
				Account a = accountService.createAccount(new Account("OwnerAccount123", "The Marwaniott", LocalDate.of(1980, 1, 1)));
				ownerService.createOwner(new Owner("marwan@themarwaniott.com", "Marwan", a));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelsystemApplication.class, args);
	}
}
