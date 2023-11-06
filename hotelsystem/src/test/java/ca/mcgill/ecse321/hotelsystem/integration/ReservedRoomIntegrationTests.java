package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservedRoomIntegrationTests {

    @Autowired
    private ReservedRoomRepository reservedRoomRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reservedRoomRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testGetAllReservations() {

    }
}
