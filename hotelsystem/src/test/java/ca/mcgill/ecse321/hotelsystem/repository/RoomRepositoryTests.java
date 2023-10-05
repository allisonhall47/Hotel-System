package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.Model.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomRepositoryTests {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testPersistAndLoadWithUniqueId() {
        Room room = new Room("Idk", 5, BedType.King, 3);
        room = roomRepository.save(room);

        // finds the room by type
        Room roomRep = roomRepository.findRoomByType("Idk");

        // asserts the retrieved room and verifies properties
        assertNotNull(roomRep);
        assertEquals(5, roomRep.getRate());
        assertEquals(3, roomRep.getCapacity());
        assertEquals(BedType.King, roomRep.getBedType());
    }
}
