package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        specificRoomRepository.deleteAll();
        roomRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    //test read by Id
    @Test
    public void testCreateAndReadReservedRoom() {
        Reservation res = new Reservation(2, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.BeforeCheckIn, null);
        res = reservationRepository.save(res);

        SpecificRoom specificRoom = new SpecificRoom(24,ViewType.Mountain, "{[=p0P_-;", true, null);
        specificRoom = specificRoomRepository.save(specificRoom);

        ReservedRoom reservedRoom = new ReservedRoom(res,specificRoom);
        reservedRoom = repo.save(reservedRoom);

        // retrieves the reserved room by reservedID
        ReservedRoom temp = repo.findReservedRoomByReservedID(reservedRoom.getReservedID());

        // asserts retrieved reserved room and verifies properties
        assertNotNull(temp);
        assertEquals(reservedRoom.getReservedID(), temp.getReservedID());
        assertEquals(reservedRoom.getSpecificRoom().getNumber(), temp.getSpecificRoom().getNumber());
        assertEquals(reservedRoom.getReservation().getReservationID(), temp.getReservation().getReservationID());
    }

    //test delete by Id
    @Test
    @Transactional
    public void testCreateAndDeleteReservedRoomById() {
        Reservation res = new Reservation(2, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.BeforeCheckIn, null);
        res = reservationRepository.save(res);

        SpecificRoom specificRoom = new SpecificRoom(24,ViewType.Mountain, "{[=p0P_-;", true, null);
        specificRoom = specificRoomRepository.save(specificRoom);

        ReservedRoom reservedRoom = new ReservedRoom(res,specificRoom);
        reservedRoom = repo.save(reservedRoom);


        ReservedRoom temp = repo.findReservedRoomByReservedID(reservedRoom.getReservedID());

        //test if saved correctly
        assertNotNull(temp);
        assertEquals(reservedRoom.getReservedID(), temp.getReservedID());
        assertEquals(reservedRoom.getSpecificRoom().getNumber(), temp.getSpecificRoom().getNumber());
        assertEquals(reservedRoom.getReservation().getReservationID(), temp.getReservation().getReservationID());

        //test if deleted
        repo.deleteByReservedID(temp.getReservedID());
        temp = repo.findReservedRoomByReservedID(reservedRoom.getReservedID());
        assertNull(temp);

    }

    //test write new attributes to an already existing row in table
    @Test
    public void testCreateAndWriteReservedRoom() {
        Reservation res = new Reservation(2, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.BeforeCheckIn, null);
        res = reservationRepository.save(res);

        SpecificRoom specificRoom = new SpecificRoom(24,ViewType.Mountain, "desc", true, null);
        specificRoom = specificRoomRepository.save(specificRoom);

        ReservedRoom reservedRoom = new ReservedRoom(res,specificRoom);
        reservedRoom = repo.save(reservedRoom);

        //create new attributes
        Reservation res2 = new Reservation(3, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.CheckedIn, null);
        res2 = reservationRepository.save(res2);

        SpecificRoom specificRoom2 = new SpecificRoom(26,ViewType.Mountain, "test", false, null);
        specificRoom2 = specificRoomRepository.save(specificRoom2);

        //get old references, attributes
        Reservation previousRes = reservedRoom.getReservation();
        SpecificRoom previousRoom = reservedRoom.getSpecificRoom();
        int previousId = reservedRoom.getReservedID();

        //set new attributes
        reservedRoom.setReservation(res2);
        reservedRoom.setSpecificRoom(specificRoom2);

        //save and compare
        reservedRoom = repo.save(reservedRoom);
        assertNotEquals(previousRes.getReservationID(), reservedRoom.getReservation().getReservationID());
        assertNotEquals(previousRoom.getNumber(), reservedRoom.getSpecificRoom().getNumber());
        assertEquals(reservedRoom.getReservedID(), previousId); //Id shouldn't change
    }

    //test read by foreign key reservation
    @Test
    public void testCreateAndReadReservedRoomByReservation_ReservationID() {
        Reservation res = new Reservation(2, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.BeforeCheckIn, null);
        res = reservationRepository.save(res);

        SpecificRoom specificRoom1 = new SpecificRoom(24,ViewType.Mountain, "{[=p0P_-;", true, null);
        specificRoom1 = specificRoomRepository.save(specificRoom1);

        SpecificRoom specificRoom2 = new SpecificRoom(42,ViewType.Mountain, "test", false, null);
        specificRoom2 = specificRoomRepository.save(specificRoom2);


        //create 2 different reservesRoom objects
        ReservedRoom reservedRoom1 = new ReservedRoom(res,specificRoom1);
        reservedRoom1 = repo.save(reservedRoom1);

        ReservedRoom reservedRoom2 = new ReservedRoom(res,specificRoom2);
        reservedRoom2 = repo.save(reservedRoom2);

        //get all reserved rooms that have corresponding reservation id
        List<ReservedRoom> temp = repo.findReservedRoomsByReservation_ReservationID(res.getReservationID());

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(reservedRoom1.getReservedID(), temp.get(0).getReservedID());
        assertEquals(reservedRoom1.getSpecificRoom().getNumber(), temp.get(0).getSpecificRoom().getNumber());
        assertEquals(reservedRoom1.getReservation().getReservationID(), temp.get(0).getReservation().getReservationID());

        assertEquals(reservedRoom2.getReservedID(), temp.get(1).getReservedID());
        assertEquals(reservedRoom2.getSpecificRoom().getNumber(), temp.get(1).getSpecificRoom().getNumber());
        assertEquals(reservedRoom2.getReservation().getReservationID(), temp.get(1).getReservation().getReservationID());
    }

    //test read by foreign key specific room
    @Test
    public void testCreateAndReadReservedRoomBySpecificRoom_Number() {
        Reservation res1 = new Reservation(2, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),23,false, CheckInStatus.BeforeCheckIn, null);
        res1 = reservationRepository.save(res1);

        Reservation res2 = new Reservation(3, LocalDate.of(1884,11,23), LocalDate.of(1992,11,23),230,true, CheckInStatus.CheckedOut, null);
        res2 = reservationRepository.save(res2);

        SpecificRoom specificRoom = new SpecificRoom(24,ViewType.Mountain, "{[=p0P_-;", true, null);
        specificRoom = specificRoomRepository.save(specificRoom);


        //create 2 different reservesRoom objects
        ReservedRoom reservedRoom1 = new ReservedRoom(res1,specificRoom);
        reservedRoom1 = repo.save(reservedRoom1);

        ReservedRoom reservedRoom2 = new ReservedRoom(res2,specificRoom);
        reservedRoom2 = repo.save(reservedRoom2);

        //get all reserved rooms that have corresponding specificRoom number
        List<ReservedRoom> temp = repo.findReservedRoomsBySpecificRoom_Number(specificRoom.getNumber());

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(reservedRoom1.getReservedID(), temp.get(0).getReservedID());
        assertEquals(reservedRoom1.getSpecificRoom().getNumber(), temp.get(0).getSpecificRoom().getNumber());
        assertEquals(reservedRoom1.getReservation().getReservationID(), temp.get(0).getReservation().getReservationID());

        assertEquals(reservedRoom2.getReservedID(), temp.get(1).getReservedID());
        assertEquals(reservedRoom2.getSpecificRoom().getNumber(), temp.get(1).getSpecificRoom().getNumber());
        assertEquals(reservedRoom2.getReservation().getReservationID(), temp.get(1).getReservation().getReservationID());
    }
}
