package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.CheckInStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import ca.mcgill.ecse321.hotelsystem.dto.ReservedRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservedRoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import ca.mcgill.ecse321.hotelsystem.repository.SpecificRoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservedRoomIntegrationTests {

    @Autowired
    private ReservedRoomRepository reservedRoomRepository;

    @Autowired
    private SpecificRoomRepository specificRoomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestRestTemplate client;

    private int reservedRoom_id;
    private int resId;
    private int specRoomId;

    @AfterAll
    public void clearDatabase() {
        reservedRoomRepository.deleteAll();
    }

    @Test
    @Order(0)
    public void testCreateReservedRoom() {
        SpecificRoom specRoom = new SpecificRoom(1, ViewType.Mountain, "des", true, null);
        specRoom = specificRoomRepository.save(specRoom);

        LocalDate checkInDate = LocalDate.of(2023,10,23);
        LocalDate checkOutDate = LocalDate.of(2023,11,1);
        Reservation reservation = new Reservation(2, checkInDate, checkOutDate, 1400, false, CheckInStatus.BeforeCheckIn, null);
        reservation = reservationRepository.save(reservation);

        ReservedRoomRequestDto request = new ReservedRoomRequestDto(reservation.getReservationID(), specRoom.getNumber());

        ResponseEntity<ReservedRoomResponseDto> response = client.postForEntity("/reservedRoom/new", request, ReservedRoomResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getReservedId() > 0);
        assertEquals(reservation.getReservationID(), response.getBody().getReservation().getReservationID());
        assertEquals(specRoom.getNumber(), response.getBody().getRoom().getNumber());

        reservedRoom_id = response.getBody().getReservedId();
        resId = reservation.getReservationID();
        specRoomId = specRoom.getNumber();
    }

    @Test
    @Order(1)
    public void testCreateInvalidReservedRoom() {
        ReservedRoomRequestDto request = new ReservedRoomRequestDto(0,0);
        ResponseEntity<String> response = client.postForEntity("/reservedRoom/new", request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "reservation not in the system.");
    }

    @Test
    @Order(2)
    public void testGetAllReservedRooms() {
        ResponseEntity<List> response = client.getForEntity("/reservedRoom", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        List<Map<String,Object>> list = response.getBody();

        assertEquals(reservedRoom_id, list.get(0).get("reservedId"));
    }

    @Test
    @Order(3)
    public void testGetReservedRoomById() {
        ResponseEntity<ReservedRoomResponseDto> response = client.getForEntity("/reservedRoom/"+reservedRoom_id, ReservedRoomResponseDto.class);

        assertEquals(reservedRoom_id, response.getBody().getReservedId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testGeInvalidReservedRoomById() {
        int id = -1;
        ResponseEntity<String> response = client.getForEntity("/reservedRoom/"+id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("reservedRoom with id does not exist", response.getBody());
    }

    @Test
    @Order(5)
    public void testGetReservedRoomsByReservationId() {
        ResponseEntity<List> response = client.getForEntity("/reservedRoom/reservation/"+resId, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        List<Map<String,Object>> list = response.getBody();

        assertEquals(reservedRoom_id, list.get(0).get("reservedId"));

    }
    @Test
    @Order(6)
    public void testGetReservedRoomsBySpecificRoomNumber() {
        ResponseEntity<List> response = client.getForEntity("/reservedRoom/specificRoom/"+specRoomId, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        List<Map<String,Object>> list = response.getBody();

        assertEquals(reservedRoom_id, list.get(0).get("reservedId"));
    }

    @Test
    @Order(7)
    public void testDeleteReservedRoom() {
        ResponseEntity<String> response = client.exchange("/reservedRoom/"+reservedRoom_id, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 = client.getForEntity("/reservedRoom/"+reservedRoom_id ,String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "reservedRoom with id does not exist");
    }

}
