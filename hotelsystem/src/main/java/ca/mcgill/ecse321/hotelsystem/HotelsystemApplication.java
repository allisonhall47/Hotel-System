package ca.mcgill.ecse321.hotelsystem;

import ca.mcgill.ecse321.hotelsystem.Model.*;
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
			Room regRoom = roomService.createRoom(new Room("Regular", 899, BedType.Queen, 2));
			Room deleuxeRoom = roomService.createRoom(new Room("Deluxe", 1299, BedType.Queen, 4));
			Room luxuryRoom = roomService.createRoom(new Room("Luxury", 1499, BedType.King, 2));
			Room suite = roomService.createRoom(new Room("Suite", 1999, BedType.King, 4));
			int num = 100;
			for (int i = 0; i < 10; i++) {
				SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "such a nice room", Boolean.TRUE, suite));
			}
			for (int i = 10; i < 20; i++) {
				SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "such a nice room", Boolean.TRUE, deleuxeRoom));
			}
			for (int i = 20; i < 30; i++) {
				SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "such a nice room", Boolean.TRUE, luxuryRoom));
			}
			for (int i = 30; i < 70; i++) {
				SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "such a nice room", Boolean.TRUE, regRoom));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelsystemApplication.class, args);
	}
}
