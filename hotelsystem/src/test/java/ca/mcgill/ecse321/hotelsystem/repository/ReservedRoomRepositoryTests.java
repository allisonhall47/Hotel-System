package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
public class ReservedRoomRepositoryTests {

    @Autowired
    private ReservedRoomRepository repo;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired

    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveReservedRoom() {
        Reservation res = new Reservation(2, Date.valueOf("223-23-23"), Date.valueOf("223-23-23"),23,false, CheckInStatus.BeforeCheckIn);

    }
}
