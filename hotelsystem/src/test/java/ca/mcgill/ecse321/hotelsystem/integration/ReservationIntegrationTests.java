package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationIntegrationTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reservationRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testGetAllReservations() {

    }

}
