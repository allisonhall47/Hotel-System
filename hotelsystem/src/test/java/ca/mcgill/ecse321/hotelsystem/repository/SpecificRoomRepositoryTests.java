package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SpecificRoomRepositoryTests {

    @Autowired
    private SpecificRoomRepository repo;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testCreateAndRetrieveReservedRoom() {
        Room room = new Room("double", 5, BedType.Double, 2);
        roomRepository.save(room);

        SpecificRoom specificRoom = new SpecificRoom(24, ViewType.Forest, "{[=p_-;", true, room);
        specificRoom = repo.save(specificRoom);

        SpecificRoom tem = repo.findSpecificRoomByNumber(specificRoom.getNumber());

        assertEquals(specificRoom.getNumber(), tem.getNumber());
        assertEquals(specificRoom.getRoom().getType(), tem.getRoom().getType());
        assertEquals(specificRoom.getView(), tem.getView());
        assertEquals(specificRoom.getDescription(), tem.getDescription());
        assertEquals(specificRoom.getOpenForUse(), tem.getOpenForUse());
    }
}
