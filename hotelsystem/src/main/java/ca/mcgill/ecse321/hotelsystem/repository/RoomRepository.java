package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Reservation, String> {
    Room findRoomByType(String type);
}
