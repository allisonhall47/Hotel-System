package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoomServiceTests {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    public void testGetAllRooms(){
        Room room1 = new Room("Suite", 999, BedType.King, 4);
        Room room2 = new Room("Regular", 499, BedType.Double, 2);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        when(roomRepository.findAll()).thenReturn(rooms);
        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        assertEquals(room1, result.get(0));
        assertEquals(room2, result.get(1));
    }

    @Test
    public void testGetAllEmptyRooms(){
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);
        HRSException e = assertThrows(HRSException.class, () -> roomService.getAllRooms());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no rooms in the system.");
    }

    @Test
    public void testGetRoomByType(){
        Room room = new Room("Suite", 999, BedType.King, 4);
        when(roomRepository.findRoomByType("Suite")).thenReturn(room);

        Room result = roomService.getRoomByType(room.getType());
        assertEquals(room, result);
    }

    @Test
    public void testGetRoomByInvalidType(){
        when(roomRepository.findRoomByType("Suite")).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> roomService.getRoomByType("Suite"));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "No rooms in the system with type Suite.");
    }

    @Test
    public void testGetRoomByNullType(){
        HRSException e = assertThrows(HRSException.class, () -> roomService.getRoomByType(""));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Please enter a valid string.");
    }

    @Test
    public void testCreateRoom1(){
        Room room = new Room("Suite", 999, BedType.King, 4);
        when(roomRepository.save(room)).thenReturn(room);
        Room result = roomService.createRoom(room);
        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testCreateRoom2(){
        Room room = new Room("Regular", 999, BedType.King, 4);
        when(roomRepository.save(room)).thenReturn(room);
        Room result = roomService.createRoom(room);
        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testCreateRoom3(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        when(roomRepository.save(room)).thenReturn(room);
        Room result = roomService.createRoom(room);
        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testCreateRoom4(){
        Room room = new Room("Deluxe", 999, BedType.King, 4);
        when(roomRepository.save(room)).thenReturn(room);
        Room result = roomService.createRoom(room);
        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testCreateRoomWithInvalidRoomType(){
        Room room = new Room("Messi", 999, BedType.King, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.createRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room type.");
    }

    @Test
    public void testCreateRoomWithNegativeRate(){
        Room room = new Room("Suite", -700, BedType.King, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.createRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room rate.");
    }

    @Test
    public void testCreateRoomWithNullBedType(){
        Room room = new Room("Suite", 999, null, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.createRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid bed type.");
    }

    @Test
    public void testCreateRoomWithNegativeCapacity(){
        Room room = new Room("Suite", 999, BedType.King, -5);
        HRSException e = assertThrows(HRSException.class, () -> roomService.createRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room capacity.");
    }

    @Test
    public void testUpdateRoom(){
        Room oldRoom = new Room("Suite", 999, BedType.King, 4);
        Room room = new Room("Suite", 1499, BedType.King, 4);
        when(roomRepository.findRoomByType("Suite")).thenReturn(oldRoom);
        when(roomRepository.save(oldRoom)).thenReturn(oldRoom);
        Room result = roomService.updateRoom(room);
        assertEquals(oldRoom, result);
    }

    @Test
    public void testUpdateRoomNonExistingRoom(){
        Room room = new Room("Suite", 1499, BedType.King, 4);
        when(roomRepository.findRoomByType("Suite")).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> roomService.updateRoom(room));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "No rooms in the system with type Suite.");
    }

    @Test
    public void testUpdateRoomWithInvalidRoomType(){
        Room room = new Room("Messi", 999, BedType.King, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.updateRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room type.");
    }

    @Test
    public void testUpdateRoomWithNegativeRate(){
        Room room = new Room("Suite", -700, BedType.King, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.updateRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room rate.");
    }

    @Test
    public void testUpdateRoomWithNullBedType(){
        Room room = new Room("Suite", 999, null, 4);
        HRSException e = assertThrows(HRSException.class, () -> roomService.updateRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid bed type.");
    }

    @Test
    public void testUpdateRoomWithNegativeCapacity(){
        Room room = new Room("Suite", 999, BedType.King, -5);
        HRSException e = assertThrows(HRSException.class, () -> roomService.updateRoom(room));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room capacity.");
    }

}
