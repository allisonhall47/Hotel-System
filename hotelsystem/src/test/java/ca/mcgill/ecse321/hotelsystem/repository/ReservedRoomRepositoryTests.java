package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservedRoomRepositoryTests {

    @Autowired
    private ReservedRoomRepository repo;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SpecificRoomRepository specificRoomRepository;

    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveReservedRoom() {
        Reservation res = new Reservation(2, Date.valueOf("1884-11-23"), Date.valueOf("1992-11-23"),23,false, CheckInStatus.BeforeCheckIn, null);
        reservationRepository.save(res);

        Room room = new Room("double", 5, BedType.Double, 2);
        roomRepository.save(room);

        SpecificRoom specificRoom = new SpecificRoom(24,ViewType.Mountain, "{[=p0P_-;", true, room);
        specificRoomRepository.save(specificRoom);

        ReservedRoom reservedRoom = new ReservedRoom(res,specificRoom);
        reservedRoom = repo.save(reservedRoom);

        ReservedRoom tem = repo.findReservedRoomByReservedID(reservedRoom.getReservedID());

        assertEquals(reservedRoom.getReservedID(), tem.getReservedID());
        assertEquals(reservedRoom.getSpecificRoom().getNumber(), tem.getSpecificRoom().getNumber());
        assertEquals(reservedRoom.getReservation().getReservationID(), tem.getReservation().getReservationID());
    }
}
