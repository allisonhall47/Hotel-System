package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.dto.*;
import ca.mcgill.ecse321.hotelsystem.repository.RoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomIntegrationTests {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TestRestTemplate client;


    @Test
    @Order(0)
    public void testGetAllEmptyRooms(){
        ResponseEntity<String> response = client.getForEntity("/rooms", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "There are no rooms in the system.");
    }

    @Test
    @Order(1)
    public void testCreateValidRoom(){
        RoomRequestDto roomRequestDto = new RoomRequestDto("Luxury", 999, BedType.King, 3);
        RoomRequestDto roomRequestDto2 = new RoomRequestDto("Suite", 1499, BedType.King, 5);
        ResponseEntity<RoomResponseDto> roomResponse = client.postForEntity("/room/create", roomRequestDto, RoomResponseDto.class);
        ResponseEntity<RoomResponseDto> roomResponse2 = client.postForEntity("/room/create", roomRequestDto2, RoomResponseDto.class);
        assertEquals(HttpStatus.CREATED, roomResponse.getStatusCode());
        assertNotNull(roomResponse.getBody());
        assertNotNull(roomResponse2.getBody());
        assertTrue(equals(roomResponse.getBody(), roomRequestDto));
    }

    @Test
    @Order(2)
    public void testGetValidRoomByType(){
        RoomRequestDto roomRequestDto = new RoomRequestDto("Luxury", 999, BedType.King, 3);
        ResponseEntity<RoomResponseDto> roomResponse = client.getForEntity("/room/Luxury", RoomResponseDto.class);
        assertEquals(HttpStatus.OK, roomResponse.getStatusCode());
        assertNotNull(roomResponse.getBody());
        assertTrue(equals(roomResponse.getBody(), roomRequestDto));
    }

    @Test
    @Order(3)
    public void testUpdateValidRoom(){
        RoomRequestDto roomRequestDto = new RoomRequestDto("Luxury", 999, BedType.King, 4);
        HttpEntity<RoomRequestDto> requestEntity = new HttpEntity<>(roomRequestDto);
        ResponseEntity<RoomResponseDto> roomResponse = client.exchange("/room/update", HttpMethod.PUT, requestEntity, RoomResponseDto.class);
        assertEquals(HttpStatus.OK, roomResponse.getStatusCode());
        assertNotNull(roomResponse.getBody());
        assertTrue(equals(roomResponse.getBody(), roomRequestDto));
    }

    @Test
    @Order(4)
    public void testGetAllRooms(){
        RoomRequestDto roomRequestDto = new RoomRequestDto("Luxury", 999, BedType.King, 4);
        RoomRequestDto roomRequestDto2 = new RoomRequestDto("Suite", 1499, BedType.King, 5);
        ResponseEntity<List> response = client.getForEntity("/rooms", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        List<Map<String, Object>> rooms = response.getBody();
        assertEquals(roomRequestDto.getType(), rooms.get(1).get("type"));
        assertEquals("King", rooms.get(1).get("bedType"));
        assertEquals(roomRequestDto.getRate(), rooms.get(1).get("rate"));
        assertEquals(roomRequestDto.getCapacity(), rooms.get(1).get("capacity"));
        assertEquals(roomRequestDto2.getType(), rooms.get(0).get("type"));
        assertEquals("King", rooms.get(0).get("bedType"));
        assertEquals(roomRequestDto2.getRate(), rooms.get(0).get("rate"));
        assertEquals(roomRequestDto2.getCapacity(), rooms.get(0).get("capacity"));
    }


    private boolean equals(RoomResponseDto roomResponseDto, RoomRequestDto roomRequestDto){
        return roomResponseDto.getType().equals(roomRequestDto.getType()) && roomResponseDto.getRate() == roomRequestDto.getRate() && roomResponseDto.getBedType().equals(roomRequestDto.getBedType()) && roomResponseDto.getCapacity() == roomRequestDto.getCapacity();
    }

}
