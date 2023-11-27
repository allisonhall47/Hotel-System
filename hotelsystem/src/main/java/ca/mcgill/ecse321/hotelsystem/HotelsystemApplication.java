package ca.mcgill.ecse321.hotelsystem;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.repository.OwnerRepository;
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
	CommandLineRunner initDatabase(@Autowired OwnerService ownerService, @Autowired RoomService roomService, @Autowired SpecificRoomService specificRoomService, @Autowired OwnerRepository ownerRepository, @Autowired AccountService accountService){
		return args -> {
			if (ownerRepository.count() == 0){
				Account a = accountService.createAccount(new Account("OwnerAccount123", "The Marwaniott", LocalDate.of(1980, 1, 1)));
				ownerService.createOwner(new Owner("marwan@themarwaniott.com", "Marwan", a));

				Room regRoom = roomService.createRoom(new Room("Regular", 450, BedType.Queen, 2));
				Room deleuxeRoom = roomService.createRoom(new Room("Deluxe", 600, BedType.Queen, 4));
				Room luxuryRoom = roomService.createRoom(new Room("Luxury", 950, BedType.King, 2));
				Room suite = roomService.createRoom(new Room("Suite", 1400, BedType.King, 4));
				int num = 100;
				for (int i = 0; i < 10; i++) {
					SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "Suite with two king beds.", Boolean.TRUE, suite));
				}
				for (int i = 10; i < 20; i++) {
					SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "Deluxe room with two queen beds.", Boolean.TRUE, deleuxeRoom));
				}
				for (int i = 20; i < 30; i++) {
					SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Mountain, "Luxury room with one king bed.", Boolean.TRUE, luxuryRoom));
				}
				for (int i = 30; i < 50; i++) {
					SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Village, "Regular room with one queen bed.", Boolean.TRUE, regRoom));
				}
				for (int i = 50; i < 70; i++) {
					SpecificRoom specificRoom = specificRoomService.createSpecificRoom(new SpecificRoom(num+i, ViewType.Forest, "Regular room with one queen bed.", Boolean.TRUE, regRoom));
				}
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HotelsystemApplication.class, args);
	}
}
