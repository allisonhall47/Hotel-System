package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SpecificRoomRepositoryTests {

    @Autowired
    private SpecificRoomRepository repo;

    @Autowired
    private RoomRepository roomRepository;

    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        roomRepository.deleteAll();
    }

    //test read by primary key id
    @Test
    public void testCreateAndRetrieveSpecificRoomById() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom = repo.save(specificRoom);

        SpecificRoom temp = repo.findSpecificRoomByNumber(specificRoom.getNumber());

        assertNotNull(temp);
        assertEquals(specificRoom.getNumber(), temp.getNumber());
        assertEquals(specificRoom.getRoom().getType(), temp.getRoom().getType());
        assertEquals(specificRoom.getView(), temp.getView());
        assertEquals(specificRoom.getDescription(), temp.getDescription());
        assertEquals(specificRoom.getOpenForUse(), temp.getOpenForUse());
    }

    //test delete by number
    @Test
    @Transactional
    public void testCreateAndDeleteByNumber() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom = repo.save(specificRoom);

        SpecificRoom temp = repo.findSpecificRoomByNumber(specificRoom.getNumber());

        //test is saved correctly
        assertNotNull(temp);
        assertEquals(specificRoom.getNumber(), temp.getNumber());
        assertEquals(specificRoom.getRoom().getType(), temp.getRoom().getType());
        assertEquals(specificRoom.getView(), temp.getView());
        assertEquals(specificRoom.getDescription(), temp.getDescription());
        assertEquals(specificRoom.getOpenForUse(), temp.getOpenForUse());

        //delete
        repo.deleteByNumber(temp.getNumber());
        assertNull(repo.findSpecificRoomByNumber(temp.getNumber()));
    }

    //test read by attribute view
    @Test
    public void testCreateAndRetrieveSpecificRoomByView() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom1 = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom1 = repo.save(specificRoom1);

        SpecificRoom specificRoom2 = new SpecificRoom(25, ViewType.Forest, "test", false, room);
        specificRoom2 = repo.save(specificRoom2);

        List<SpecificRoom> temp = repo.findSpecificRoomsByView(specificRoom1.getView());

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(specificRoom1.getNumber(), temp.get(0).getNumber());
        assertEquals(specificRoom1.getRoom().getType(), temp.get(0).getRoom().getType());
        assertEquals(specificRoom1.getView(), temp.get(0).getView());
        assertEquals(specificRoom1.getDescription(), temp.get(0).getDescription());
        assertEquals(specificRoom1.getOpenForUse(), temp.get(0).getOpenForUse());

        assertEquals(specificRoom2.getNumber(), temp.get(1).getNumber());
        assertEquals(specificRoom2.getRoom().getType(), temp.get(1).getRoom().getType());
        assertEquals(specificRoom2.getView(), temp.get(1).getView());
        assertEquals(specificRoom2.getDescription(), temp.get(1).getDescription());
        assertEquals(specificRoom2.getOpenForUse(), temp.get(1).getOpenForUse());
    }

    //test read by attribute openForUse
    @Test
    public void testCreateAndRetrieveSpecificRoomByOpenForUseIsTrue() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom1 = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom1 = repo.save(specificRoom1);

        SpecificRoom specificRoom2 = new SpecificRoom(25, ViewType.Mountain, "test", true, room);
        specificRoom2 = repo.save(specificRoom2);

        List<SpecificRoom> temp = repo.findSpecificRoomsByOpenForUseIsTrue();

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(specificRoom1.getNumber(), temp.get(0).getNumber());
        assertEquals(specificRoom1.getRoom().getType(), temp.get(0).getRoom().getType());
        assertEquals(specificRoom1.getView(), temp.get(0).getView());
        assertEquals(specificRoom1.getDescription(), temp.get(0).getDescription());
        assertEquals(specificRoom1.getOpenForUse(), temp.get(0).getOpenForUse());

        assertEquals(specificRoom2.getNumber(), temp.get(1).getNumber());
        assertEquals(specificRoom2.getRoom().getType(), temp.get(1).getRoom().getType());
        assertEquals(specificRoom2.getView(), temp.get(1).getView());
        assertEquals(specificRoom2.getDescription(), temp.get(1).getDescription());
        assertEquals(specificRoom2.getOpenForUse(), temp.get(1).getOpenForUse());
    }

    //test read by attribute openForUse
    @Test
    public void testCreateAndRetrieveSpecificRoomByOpenForUseIsFalse() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom1 = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", false, room);
        specificRoom1 = repo.save(specificRoom1);

        SpecificRoom specificRoom2 = new SpecificRoom(25, ViewType.Mountain, "test", false, room);
        specificRoom2 = repo.save(specificRoom2);

        List<SpecificRoom> temp = repo.findSpecificRoomsByOpenForUseIsFalse();

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(specificRoom1.getNumber(), temp.get(0).getNumber());
        assertEquals(specificRoom1.getRoom().getType(), temp.get(0).getRoom().getType());
        assertEquals(specificRoom1.getView(), temp.get(0).getView());
        assertEquals(specificRoom1.getDescription(), temp.get(0).getDescription());
        assertEquals(specificRoom1.getOpenForUse(), temp.get(0).getOpenForUse());

        assertEquals(specificRoom2.getNumber(), temp.get(1).getNumber());
        assertEquals(specificRoom2.getRoom().getType(), temp.get(1).getRoom().getType());
        assertEquals(specificRoom2.getView(), temp.get(1).getView());
        assertEquals(specificRoom2.getDescription(), temp.get(1).getDescription());
        assertEquals(specificRoom2.getOpenForUse(), temp.get(1).getOpenForUse());
    }

    //test read by foreign key room
    @Test
    public void testCreateAndRetrieveSpecificRoomByRoom_Type() {
        Room room = new Room("double", 5, BedType.Double, 2);
        room = roomRepository.save(room);

        SpecificRoom specificRoom1 = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom1 = repo.save(specificRoom1);

        SpecificRoom specificRoom2 = new SpecificRoom(42, ViewType.Mountain, "test", false, room);
        specificRoom2 = repo.save(specificRoom2);

        List<SpecificRoom> temp = repo.findSpecificRoomsByRoom_Type(specificRoom1.getRoom().getType());

        //compare all
        assertNotEquals(0, temp.size());
        assertEquals(specificRoom1.getRoom().getType(), temp.get(0).getRoom().getType());
        assertEquals(specificRoom1.getNumber(), temp.get(0).getNumber());
        assertEquals(specificRoom1.getView(), temp.get(0).getView());
        assertEquals(specificRoom1.getDescription(), temp.get(0).getDescription());
        assertEquals(specificRoom1.getOpenForUse(), temp.get(0).getOpenForUse());

        assertEquals(specificRoom2.getRoom().getType(), temp.get(1).getRoom().getType());
        assertEquals(specificRoom2.getNumber(), temp.get(1).getNumber());
        assertEquals(specificRoom2.getView(), temp.get(1).getView());
        assertEquals(specificRoom2.getDescription(), temp.get(1).getDescription());
        assertEquals(specificRoom2.getOpenForUse(), temp.get(1).getOpenForUse());
    }

    //test write new attributes
    @Test
    public void testCreateAndWriteSpecificRoom() {
        Room room1 = new Room("double", 5, BedType.Double, 2);
        room1 = roomRepository.save(room1);

        SpecificRoom specificRoom = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room1);
        specificRoom = repo.save(specificRoom);

        //temp will contain the old attributes
        SpecificRoom temp = repo.findSpecificRoomByNumber(specificRoom.getNumber());

        //test if it was properly saved first
        assertNotNull(temp);
        assertEquals(specificRoom.getNumber(), temp.getNumber());
        assertEquals(specificRoom.getRoom().getType(), temp.getRoom().getType());
        assertEquals(specificRoom.getView(), temp.getView());
        assertEquals(specificRoom.getDescription(), temp.getDescription());
        assertEquals(specificRoom.getOpenForUse(), temp.getOpenForUse());

        //change with new attributes
        Room room2 = new Room("king", 5, BedType.King, 4);
        room2 =  roomRepository.save(room2);
        specificRoom.setRoom(room2);
        specificRoom.setDescription("test");
        specificRoom.setNumber(42);
        specificRoom.setView(ViewType.Mountain);
        specificRoom.setOpenForUse(false);

        //compare
        assertNotEquals(temp.getRoom().getType(), specificRoom.getRoom().getType());
        assertNotEquals(temp.getNumber(), specificRoom.getNumber());
        assertNotEquals(temp.getDescription(), specificRoom.getDescription());
        assertNotEquals(temp.getView(), specificRoom.getView());
        assertNotEquals(temp.getOpenForUse(), specificRoom.getOpenForUse());
    }
}
