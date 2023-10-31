package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedRoomRepository extends CrudRepository<ReservedRoom, Integer> {
    ReservedRoom findReservedRoomByReservedID(int id);
    List<ReservedRoom> findReservedRoomsByReservation_ReservationID(int id);
    List<ReservedRoom> findReservedRoomsBySpecificRoom_Number(int id);
    void deleteByReservedID(int id);

}
