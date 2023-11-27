package ca.mcgill.ecse321.hotelsystem.integration;


import ca.mcgill.ecse321.hotelsystem.Model.BedType;
import ca.mcgill.ecse321.hotelsystem.Model.Room;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.SpecificRoomResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.RoomRepository;
import ca.mcgill.ecse321.hotelsystem.repository.SpecificRoomRepository;
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
public class SpecificRoomIntegrationTests {
    @Autowired
    private SpecificRoomRepository specificRoomRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void clearDatabase() {
        specificRoomRepository.deleteAll();
        Room room = new Room("Suite", 950, BedType.King, 4);
        roomRepository.save(room);
    }

    @Test
    @Order(0)
    public void testCreateValidSpecificRoom(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        SpecificRoomRequestDto specificRoomRequestDto2 = new SpecificRoomRequestDto(103, ViewType.Forest, "Haaland's Room", "Suite");
        ResponseEntity<SpecificRoomResponseDto> specificRoomResponse =  client.postForEntity("/specificRoom/create", specificRoomRequestDto, SpecificRoomResponseDto.class);
        ResponseEntity<SpecificRoomResponseDto> specificRoomResponse2 =  client.postForEntity("/specificRoom/create", specificRoomRequestDto2, SpecificRoomResponseDto.class);
        assertEquals(HttpStatus.CREATED, specificRoomResponse.getStatusCode());
        assertNotNull(specificRoomResponse.getBody());
        assertTrue(equals(specificRoomResponse.getBody(), specificRoomRequestDto));
    }

    @Test
    @Order(1)
    public void testGetSpecificRoomByNumber(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        ResponseEntity<SpecificRoomResponseDto> specificRoomResponse =  client.getForEntity("/specificRoom/number/101", SpecificRoomResponseDto.class);
        assertEquals(HttpStatus.OK, specificRoomResponse.getStatusCode());
        assertNotNull(specificRoomResponse.getBody());
        assertTrue(equals(specificRoomResponse.getBody(), specificRoomRequestDto));
    }

    @Test
    @Order(2)
    public void testGetSpecificRoomsByType(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        SpecificRoomRequestDto specificRoomRequestDto2 = new SpecificRoomRequestDto(103, ViewType.Forest, "Haaland's Room", "Suite");
        ResponseEntity<List> response = client.getForEntity("/specificRoom/type/Suite", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        List<Map<String, Object>> specificRooms = response.getBody();
        assertEquals(specificRoomRequestDto.getRoomType(), specificRooms.get(0).get("roomType"));
        assertEquals(specificRoomRequestDto.getDescription(), specificRooms.get(0).get("description"));
        assertEquals("Forest", specificRooms.get(0).get("view"));
        assertEquals(specificRoomRequestDto.getNumber(), specificRooms.get(0).get("number"));
        assertEquals(specificRoomRequestDto2.getRoomType(), specificRooms.get(1).get("roomType"));
        assertEquals(specificRoomRequestDto2.getDescription(), specificRooms.get(1).get("description"));
        assertEquals("Forest", specificRooms.get(1).get("view"));
        assertEquals(specificRoomRequestDto2.getNumber(), specificRooms.get(1).get("number"));
    }

    @Test
    @Order(3)
    public void testGetSpecificRoomsOpenForUse(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        SpecificRoomRequestDto specificRoomRequestDto2 = new SpecificRoomRequestDto(103, ViewType.Forest, "Haaland's Room", "Suite");
        ResponseEntity<List> response = client.getForEntity("/specificRoom/openForUse", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        List<Map<String, Object>> specificRooms = response.getBody();
        assertEquals(specificRoomRequestDto.getRoomType(), specificRooms.get(0).get("roomType"));
        assertEquals(specificRoomRequestDto.getDescription(), specificRooms.get(0).get("description"));
        assertEquals("Forest", specificRooms.get(0).get("view"));
        assertEquals(specificRoomRequestDto.getNumber(), specificRooms.get(0).get("number"));
        assertEquals(specificRoomRequestDto2.getRoomType(), specificRooms.get(1).get("roomType"));
        assertEquals(specificRoomRequestDto2.getDescription(), specificRooms.get(1).get("description"));
        assertEquals("Forest", specificRooms.get(1).get("view"));
        assertEquals(specificRoomRequestDto2.getNumber(), specificRooms.get(1).get("number"));
    }

    @Test
    @Order(4)
    public void testGetSpecificRoomsByView(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        SpecificRoomRequestDto specificRoomRequestDto2 = new SpecificRoomRequestDto(103, ViewType.Forest, "Haaland's Room", "Suite");
        ResponseEntity<List> response = client.getForEntity("/specificRoom/view/Forest", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        List<Map<String, Object>> specificRooms = response.getBody();
        assertEquals(specificRoomRequestDto.getRoomType(), specificRooms.get(0).get("roomType"));
        assertEquals(specificRoomRequestDto.getDescription(), specificRooms.get(0).get("description"));
        assertEquals("Forest", specificRooms.get(0).get("view"));
        assertEquals(specificRoomRequestDto.getNumber(), specificRooms.get(0).get("number"));
        assertEquals(specificRoomRequestDto2.getRoomType(), specificRooms.get(1).get("roomType"));
        assertEquals(specificRoomRequestDto2.getDescription(), specificRooms.get(1).get("description"));
        assertEquals("Forest", specificRooms.get(1).get("view"));
        assertEquals(specificRoomRequestDto2.getNumber(), specificRooms.get(1).get("number"));
    }

    @Test
    @Order(5)
    public void getAllSpecificRooms(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Forest, "Messi's Room", "Suite");
        SpecificRoomRequestDto specificRoomRequestDto2 = new SpecificRoomRequestDto(103, ViewType.Forest, "Haaland's Room", "Suite");
        ResponseEntity<List> response = client.getForEntity("/specificRooms", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 2);
        List<Map<String, Object>> specificRooms = response.getBody();
        assertEquals(specificRoomRequestDto.getRoomType(), specificRooms.get(0).get("roomType"));
        assertEquals(specificRoomRequestDto.getDescription(), specificRooms.get(0).get("description"));
        assertEquals("Forest", specificRooms.get(0).get("view"));
        assertEquals(specificRoomRequestDto.getNumber(), specificRooms.get(0).get("number"));
        assertEquals(specificRoomRequestDto2.getRoomType(), specificRooms.get(1).get("roomType"));
        assertEquals(specificRoomRequestDto2.getDescription(), specificRooms.get(1).get("description"));
        assertEquals("Forest", specificRooms.get(1).get("view"));
        assertEquals(specificRoomRequestDto2.getNumber(), specificRooms.get(1).get("number"));
    }

    @Test
    @Order(6)
    public void testUpdateSpecificRoom(){
        SpecificRoomRequestDto specificRoomRequestDto = new SpecificRoomRequestDto(101, ViewType.Mountain, "Ronaldo's Room", "Suite");
        HttpEntity<SpecificRoomRequestDto> requestEntity = new HttpEntity<>(specificRoomRequestDto);
        ResponseEntity<SpecificRoomResponseDto> specificRoomResponse = client.exchange("/specificRoom/update", HttpMethod.PUT, requestEntity, SpecificRoomResponseDto.class);
        assertEquals(HttpStatus.OK, specificRoomResponse.getStatusCode());
        assertNotNull(specificRoomResponse.getBody());
        assertTrue(equals(specificRoomResponse.getBody(), specificRoomRequestDto));
    }

    @Test
    @Order(7)
    public void testDeleteSpecificRoom(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/specificRoom/delete/101", HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public Boolean equals(SpecificRoomResponseDto specificRoomResponseDto, SpecificRoomRequestDto specificRoomRequestDto){
        return specificRoomResponseDto.getRoomType().equals(specificRoomRequestDto.getRoomType()) && specificRoomResponseDto.getDescription().equals(specificRoomRequestDto.getDescription()) && specificRoomResponseDto.getView().equals(specificRoomRequestDto.getView()) && specificRoomResponseDto.getNumber() == specificRoomRequestDto.getNumber();
    }
}
