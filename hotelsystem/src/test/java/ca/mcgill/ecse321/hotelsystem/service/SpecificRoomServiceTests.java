package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.SpecificRoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SpecificRoomServiceTests {
    @Mock
    private SpecificRoomRepository specificRoomRepository;

    @InjectMocks
    private SpecificRoomService specificRoomService;

    @Test
    public void testGetAllSpecificRooms(){
        Room room = new Room("Suite", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findAll()).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.getAllSpecificRooms();

        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testGetAllEmptySpecificRooms(){
        List<SpecificRoom> specificRooms = new ArrayList<>();
        when(specificRoomRepository.findAll()).thenReturn(specificRooms);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.getAllSpecificRooms());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no specific rooms in the system.");
    }

    @Test
    public void testFindSpecificRoomByNumber(){
        Room room = new Room("Suite", 999, BedType.King, 4);
        SpecificRoom specificRoom = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.findSpecificRoomByNumber(101)).thenReturn(specificRoom);
        SpecificRoom result = specificRoomService.findSpecificRoomByNumber(101);
        assertEquals(specificRoom, result);
    }

    @Test
    public void testFindSpecificRoomByInvalidNumber(){
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomByNumber(-5));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid number.");
    }

    @Test
    public void testFindSpecificRoomByNonExistingNumber(){
        when(specificRoomRepository.findSpecificRoomByNumber(102)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomByNumber(102));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There is no specific room in the system with number "+ 102 + ".");
    }

    @Test
    public void testFindSpecificRoomByRoomType1(){
        Room room = new Room("Suite", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Suite")).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsByRoomType("Suite");
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindSpecificRoomByRoomType2(){
        Room room = new Room("Deluxe", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Deluxe")).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsByRoomType("Deluxe");
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindSpecificRoomByRoomType3(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Luxury")).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsByRoomType("Luxury");
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindSpecificRoomByRoomType4(){
        Room room = new Room("Regular", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Regular")).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsByRoomType("Regular");
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindSpecificRoomByInvalidRoomType(){
        Room room = new Room("Messi", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Messi")).thenReturn(specificRooms);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomsByRoomType("Messi"));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid room type.");
    }

    @Test
    public void testFindNonExistentSpecificRoomByRoomType(){
        when(specificRoomRepository.findSpecificRoomsByRoom_Type("Luxury")).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomsByRoomType("Luxury"));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no specific rooms in the system with type Luxury.");
    }

    @Test
    public void testFindSpecificRoomsByView(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByView(ViewType.Forest)).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsByView(ViewType.Forest);
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindNonExistentSpecificRoomsByView(){
        when(specificRoomRepository.findSpecificRoomsByView(ViewType.Forest)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomsByView(ViewType.Forest));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no specific rooms in the system with this view type.");
    }

    @Test
    public void testFindSpecificRoomsOpenForUse(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom2 = new SpecificRoom(102, ViewType.Forest, "Haaland's room", Boolean.TRUE, room);
        List<SpecificRoom> specificRooms = new ArrayList<>();
        specificRooms.add(specificRoom1);
        specificRooms.add(specificRoom2);
        when(specificRoomRepository.findSpecificRoomsByOpenForUseIsTrue()).thenReturn(specificRooms);
        List<SpecificRoom> result = specificRoomService.findSpecificRoomsOpenForUse();
        assertEquals(2, result.size());
        assertEquals(specificRoom1, result.get(0));
        assertEquals(specificRoom2, result.get(1));
    }

    @Test
    public void testFindNonExistentSpecificRoomsOpenForUse(){
        when(specificRoomRepository.findSpecificRoomsByOpenForUseIsTrue()).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.findSpecificRoomsOpenForUse());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no specific rooms open for use.");
    }

    @Test
    public void testDeleteSpecificRoomByNumber(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom1 = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.findSpecificRoomByNumber(101)).thenReturn(specificRoom1);
        assertDoesNotThrow(() -> specificRoomService.deleteSpecificRoomByNumber(101));
    }

    @Test
    public void testDeleteSpecificRoomByInvalidNumber(){
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.deleteSpecificRoomByNumber(-1));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid number.");
    }
    @Test
    public void testDeleteNonExistentSpecificRoomByNumber(){
        when(specificRoomRepository.findSpecificRoomByNumber(101)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.deleteSpecificRoomByNumber(101));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There is no specific room in the system with number "+ 101 + ".");
    }

    @Test
    public void testCreateSpecificRoom(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.save(specificRoom)).thenReturn(specificRoom);
        SpecificRoom result = specificRoomService.createSpecificRoom(specificRoom);
        assertNotNull(result);
        assertEquals(specificRoom, result);
    }

    @Test
    public void testCreateSpecificRoomWithInvalidNumber(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom = new SpecificRoom(-5, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.save(specificRoom)).thenReturn(specificRoom);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.createSpecificRoom(specificRoom));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid number.");;
    }

    @Test
    public void testUpdateSpecificRoom(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom oldSpecificRoom = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom = new SpecificRoom(101, ViewType.Mountain, "Haaland's room", Boolean.TRUE, room);
        when(specificRoomRepository.findSpecificRoomByNumber(101)).thenReturn(oldSpecificRoom);
        when(specificRoomRepository.save(oldSpecificRoom)).thenReturn(oldSpecificRoom);
        SpecificRoom result = specificRoomService.updateSpecificRoom(specificRoom);
        assertEquals(oldSpecificRoom, result);
    }
    @Test
    public void testUpdateSpecificRoomWithInvalidNumber(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom oldSpecificRoom = new SpecificRoom(-5, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        SpecificRoom specificRoom = new SpecificRoom(-5, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.findSpecificRoomByNumber(-5)).thenReturn(oldSpecificRoom);
        when(specificRoomRepository.save(oldSpecificRoom)).thenReturn(oldSpecificRoom);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.updateSpecificRoom(specificRoom));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid number.");;
    }

    @Test
    public void testUpdateNonExistentSpecificRoom(){
        Room room = new Room("Luxury", 999, BedType.King, 4);
        SpecificRoom specificRoom = new SpecificRoom(101, ViewType.Forest, "Messi's room", Boolean.TRUE, room);
        when(specificRoomRepository.findSpecificRoomByNumber(-5)).thenReturn(null);
        HRSException e = assertThrows(HRSException.class, () -> specificRoomService.updateSpecificRoom(specificRoom));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There is no specific room in the system with number "+ specificRoom.getNumber() + ".");;
    }
}
