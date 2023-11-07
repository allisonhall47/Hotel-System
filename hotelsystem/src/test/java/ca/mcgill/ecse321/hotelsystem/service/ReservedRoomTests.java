package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ReservedRoomTests {

    @Mock
    private ReservedRoomRepository reservedRoomRepository;

    @InjectMocks
    private ReservedRoomService reservedRoomService;

    //TODO maybe change arguments to the service method, make it just reservedRoom object
    @Test
    public void testCreateValidReservedRoom() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);

        ReservedRoom resRoom = new ReservedRoom(res, room);

        when(reservedRoomRepository.save(resRoom)).thenReturn(resRoom);

        List<ReservedRoom> list = new ArrayList<>(); //empty list
        when(reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(room.getNumber())).thenReturn(list);

        ReservedRoom out = reservedRoomService.createReservedRoom(resRoom);

        assertEquals(resRoom, out);
        verify(reservedRoomRepository, times(2)).save(resRoom);


        //when(reservedRoomService.save()).thenReturn();

    }

    //already tested in specificRoom
//    @Test
//    public void testCreateInValidReservationReservedRoom() {
//        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
//        Reservation res = new Reservation(4, Date.valueOf("1990-03-03"), Date.valueOf("1990-03-06"), 5, false, CheckInStatus.BeforeCheckIn, null);
//    }
//
//    @Test
//    public void testCreateInValidSpecRoomReservedRoom() {
//        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
//        Reservation res = new Reservation(4, Date.valueOf("1990-03-03"), Date.valueOf("1990-03-06"), 5, false, CheckInStatus.BeforeCheckIn, null);
//    }

    @Test
    public void testAssignValidReservedRoomToReservation() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        ReservedRoom resRoom = new ReservedRoom(null, room);

        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom2 = new ReservedRoom(res, room);

        Reservation res2 = new Reservation(4, LocalDate.of(1990,3,7), LocalDate.of(1990,3,10), 5, false, CheckInStatus.BeforeCheckIn, null);
        List<ReservedRoom> list = new ArrayList<>();
        list.add(resRoom2);
        list.add(resRoom);

        when(reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(room.getNumber())).thenReturn(list);
        when(reservedRoomRepository.save(resRoom)).thenReturn(resRoom);

        ReservedRoom out = reservedRoomService.assignReservedRoomToReservation(res2, resRoom);

        assertNotNull(out);
        assertEquals(res2, out.getReservation());
    }

    @Test
    public void testAssignInValidReservedRoomToReservation() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4,LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom = new ReservedRoom(res, room);

        Reservation res2 = new Reservation(4, LocalDate.of(1990,3,4), LocalDate.of(1990,3,7), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom2 = new ReservedRoom(null, room);

        List<ReservedRoom> list = new ArrayList<>();
        list.add(resRoom2);
        list.add(resRoom);

        when(reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(room.getNumber())).thenReturn(list);

        HRSException e = assertThrows(HRSException.class, () -> reservedRoomService.assignReservedRoomToReservation(res2, resRoom2));
        assertEquals(e.getMessage(), "a reservation with conflicting check-in and check-out dates exists");
        assertEquals(e.getStatus(), HttpStatus.CONFLICT);
    }

    @Test
    public void testGetAllReservedRooms() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom = new ReservedRoom(res, room);

        Reservation res2 = new Reservation(4, LocalDate.of(1990,3,4), LocalDate.of(1990,3,7), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom2 = new ReservedRoom(res2, room);

        List<ReservedRoom> list = new ArrayList<>();
        list.add(resRoom2);
        list.add(resRoom);

        when(reservedRoomRepository.findAll()).thenReturn(list);

        List<ReservedRoom> out = reservedRoomService.getAllReservedRooms();

        assertEquals(list, out);
    }

    @Test
    public void testGetReservedRoomById() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,4), LocalDate.of(1990,3,7), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom = new ReservedRoom(res, room);

        when(reservedRoomRepository.findReservedRoomByReservedID(resRoom.getReservedID())).thenReturn(resRoom);

        ReservedRoom out = reservedRoomService.getReservedRoomById(resRoom.getReservedID());

        assertEquals(resRoom, out);
    }

    @Test
    public void testGetReservedRoomByInvalidId() {
        int id = -1;

        when(reservedRoomRepository.findReservedRoomByReservedID(id)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> reservedRoomService.getReservedRoomById(id));
        assertEquals(e.getMessage(), "reservedRoom with id does not exist");
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetReservedRoomsByReservationId() {
        SpecificRoom room = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4,LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom = new ReservedRoom(res, room);

        List<ReservedRoom> list = new ArrayList<>();
        list.add(resRoom);

        when(reservedRoomRepository.findReservedRoomsByReservation_ReservationID(res.getReservationID())).thenReturn(list);

        List<ReservedRoom> out = reservedRoomService.getReservedRoomsByReservation(res.getReservationID());

        assertEquals(list, out);
    }

    @Test
    public void testGetReservedRoomsByInvalidReservationId() {
        int id = -1;

        when(reservedRoomRepository.findReservedRoomsByReservation_ReservationID(id)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> reservedRoomService.getReservedRoomsByReservation(id));
        assertEquals(e.getMessage(), "no reserved room for given reservation with id: " + id);
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetReservedRoomsBySpecRoomNumber() {
        SpecificRoom specRoom = new SpecificRoom(20, ViewType.Mountain, "sdfs", true, null);
        Reservation res = new Reservation(4, LocalDate.of(1990,3,3), LocalDate.of(1990,3,6), 5, false, CheckInStatus.BeforeCheckIn, null);
        ReservedRoom resRoom = new ReservedRoom(res, specRoom);

        List<ReservedRoom> list = new ArrayList<>();
        list.add(resRoom);
        when(reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber())).thenReturn(list);

        List<ReservedRoom> out = reservedRoomService.getReservedRoomsBySpecRoom(specRoom);

        assertEquals(list, out);
    }

    @Test
    public void testGetReservedRoomsByInvalidSpecRoomNumber() {
        SpecificRoom specRoom = null;

        //when(specificRoomRepository.findSpecificRoomByNumber(number)).thenReturn(null);
        //when(reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber())).thenReturn(list);

        HRSException e = assertThrows(HRSException.class, () -> reservedRoomService.getReservedRoomsBySpecRoom(specRoom));
        assertEquals(e.getMessage(), "specific room does not exist");
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testDeleteReservedRoom() {
        ReservedRoom room = new ReservedRoom(null, null);

        when(reservedRoomRepository.findReservedRoomByReservedID(room.getReservedID())).thenReturn(room);
        assertDoesNotThrow(() -> reservedRoomService.deleteReservedRoom(room));
    }

    @Test
    public void testDeleteInvalidReservedRoom() {
        ReservedRoom room = new ReservedRoom(null, null);

        when(reservedRoomRepository.findReservedRoomByReservedID(room.getReservedID())).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> reservedRoomService.deleteReservedRoom(room));
        assertEquals(e.getMessage(), "no reserved room with id doesn't exist");
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
}
