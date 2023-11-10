package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ShiftIntegrationTests {

      /**
       * private class for valid integration testing
       */
      private class ShiftSet {

            public LocalDate date = LocalDate.of(1995, 6,7);
            public Time startTime = Time.valueOf("07:30:00");
            public Time endTime = Time.valueOf("09:30:00");

            public int getShiftID() {
                  return shiftID;
            }

            public void setShiftID(int shiftID) {
                  this.shiftID = shiftID;
            }

            public int shiftID;

            public int getShiftID1() {
                  return shiftID1;
            }

            public void setShiftID1(int shiftID1) {
                  this.shiftID1 = shiftID1;
            }

            public int shiftID1 = -1;

            public Time endTime1 = Time.valueOf("06:30:00");

            private String employeeEmail;
            public String getEmployeeEmail() {
                  return employeeEmail;
            }

            public void setEmployeeEmail(String employeeEmail) {
                  this.employeeEmail = employeeEmail;
            }

            public LocalDate getDate() {
                  return date;
            }

            public void setDate(LocalDate date) {
                  this.date = date;
            }

            public Time getStartTime() {
                  return startTime;
            }

            public void setStartTime(Time startTime) {
                  this.startTime = startTime;
            }

            public Time getEndTime() {
                  return endTime;
            }

            public void setEndTime(Time endTime) {
                  this.endTime = endTime;
            }

      }

      private ShiftSet shiftSet;

      private Shift shiftResponseComplete;

      @Autowired
      private TestRestTemplate client;
      @Autowired
      private ShiftRepository shiftRepository;

      @Autowired
      private EmployeeRepository employeeRepository;

      /**
       * initializes our setup
       */
      @BeforeAll
      public void setupShiftSet() {
            this.shiftSet = new ShiftSet();
      }

      /**
       * clears repository before execution of tests
       */
      @AfterAll
      public void clearDatabase() {
            shiftRepository.deleteAll();
            employeeRepository.deleteAll();
      }

      /**
       * saves employee in repository for tests
       */
      @BeforeEach
      public void setupTest() {
            Employee employee = new Employee();
            employee.setEmail("janegill@gmail.com");
            employeeRepository.save(employee);
            shiftSet.setEmployeeEmail(employee.getEmail());
      }
      /**
       * Test that gets all empty shifts.
       */
      @Test
      @Order(0)
      public void testGetAllEmptyShifts() {
            ResponseEntity<String> response = client.getForEntity("/shifts/", String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(response.getBody(), "There are no shifts in the system.");
      }

      /**
       * Tests the creation of a valid shift.
       */
      @Test
      @Order(1)
      public void testCreateValidShift() {
            ShiftRequestDto shiftRequest = new ShiftRequestDto(shiftSet.startTime,shiftSet.endTime,shiftSet.date);
            shiftRequest.setEmployeeEmail(shiftSet.getEmployeeEmail());
            ResponseEntity<ShiftResponseDto> shiftResponse = client.postForEntity("/shift/create", shiftRequest, ShiftResponseDto.class);

            assertEquals(HttpStatus.CREATED,shiftResponse.getStatusCode());
            assertNotNull(shiftResponse.getBody());
            assertTrue(equals(shiftResponse.getBody(), shiftSet));
            shiftSet.setEmployeeEmail(shiftResponse.getBody().getEmployeeEmail());
            shiftSet.setShiftID(shiftResponse.getBody().getShiftId());
      }

      /**
       * private method to determine equality of a mock shiftSet object and a response object
       * @param response : response object
       * @param s : shiftSet object
       * @return if they are equal
       */
      private boolean equals(ShiftResponseDto response, ShiftSet s) {
            boolean a = response.getDate().equals(s.getDate());
            boolean b = response.getStartTime().equals(s.getStartTime());
            boolean c = response.getEndTime().equals(s.getEndTime());
            return (a && b && c);
      }

      /**
       * Tests creating a shift that has a end time after the start time.
       */
      @Test
      @Order(2)
      public void testCreateInvalidTimeShift(){
            ShiftRequestDto request = new ShiftRequestDto(shiftSet.startTime,shiftSet.endTime1,shiftSet.date);
            ResponseEntity<String> response = client.postForEntity("/shift/create", request, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(response.getBody(), "Invalid start/end times.");

      }
      /**
       * Tests creating a shift that has no fields.
       */
      @Test
      @Order(3)
      public void testCreateInvalidEmptyFieldShift() {
            ShiftRequestDto request = new ShiftRequestDto();
            ResponseEntity<String> response = client.postForEntity("/shift/create", request, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(response.getBody(), "Empty fields are present.");
      }

      /**
       * Tests updating a shift
       */
      @Test
      @Order(4)
      public void testValidUpdateShift() {
            ShiftRequestDto request = new ShiftRequestDto(Time.valueOf("08:30:00"),shiftSet.getEndTime(),shiftSet.getDate());
            HttpEntity<ShiftRequestDto> shiftEntity = new HttpEntity<>(request);
            ResponseEntity<ShiftResponseDto> response = client.exchange("/shift/"+shiftSet.getShiftID(), HttpMethod.PUT, shiftEntity, ShiftResponseDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(response.getBody().getDate(), shiftSet.date);
            assertEquals(response.getBody().getEndTime(), shiftSet.getEndTime());
            shiftSet.setStartTime(response.getBody().getStartTime());
      }

      /*
       * Tests an invalid start/end time when updating.
       */
      @Test
      @Order(5)
      public void testInvalidTimesUpdateShift() {
            ShiftRequestDto request = new ShiftRequestDto(shiftSet.getStartTime(),shiftSet.endTime1,shiftSet.getDate());
            HttpEntity<ShiftRequestDto> shiftEntity = new HttpEntity<>(request);
            ResponseEntity<String> response = client.exchange("/shift/"+shiftSet.getShiftID(), HttpMethod.PUT, shiftEntity, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(response.getBody(), "Invalid start/end times.");
      }

      /*
       * Tests updating a shift with a null field.
       */
      @Test
      @Order(6)
      public void testInvalidUpdateShiftNullFields() {
            ShiftRequestDto request = new ShiftRequestDto(shiftSet.getStartTime(),null,shiftSet.getDate());
            HttpEntity<ShiftRequestDto> shiftEntity = new HttpEntity<>(request);
            ResponseEntity<String> response = client.exchange("/shift/"+shiftSet.getShiftID(), HttpMethod.PUT, shiftEntity, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(response.getBody(), "Empty fields are present.");
      }

      /*
       * Tests getting a valid shift with ID.
       */
      @Test
      @Order(7)
      public void testGetValidShiftByID() {
            ResponseEntity<ShiftResponseDto> response = client.getForEntity("/shift/get/"+shiftSet.getShiftID(), ShiftResponseDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(equals(response.getBody(), shiftSet));
      }
      /*
       * Tests getting a invalid shift, due to a negative ID.
       */
      @Test
      @Order(8)
      public void testGetInvalidShiftByInvalidID() {
            ResponseEntity<String> response = client.getForEntity("/shift/get/"+shiftSet.shiftID1, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(response.getBody(), "Invalid shift ID.");
      }

      /**
       * Tests getting a list of shifts with an employe eemail.
       */
      @Test
      @Order(9)
      public void testGetValidShiftsByEmployeeEmail() {
            ResponseEntity<List> response = client.getForEntity("/shifts/get/"+shiftSet.getEmployeeEmail(), List.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size()); // this is returning 0
            List<Map<String,Object>> shift = response.getBody();
            assertEquals(shift.get(0).get("startTime"), shiftSet.getStartTime().toString());
            assertEquals(shift.get(0).get("date"), shiftSet.getDate().toString());
            assertEquals(shift.get(0).get("endTime"), shiftSet.getEndTime().toString());
            assertEquals(shift.get(0).get("shiftId"), shiftSet.getShiftID());
      }

      /**
       * Asserts an error when there are no shifts associated with a email.
       */
      @Test
      @Order(10)
      public void testGetEmptyShiftsByEmployeeEmail() {
            ResponseEntity<String> response = client.getForEntity("/shifts/get/" + "johndoe@gmail.com", String.class);
            assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
            assertEquals(response.getBody(), "No shifts found for this email.");
      }

      /**
       * Tests getting valid shifts by date.
       */
      @Test
      @Order(11)
      public void testGetValidShiftsByDate() {
            ResponseEntity<List> response = client.getForEntity("/shifts/date/get/" + shiftSet.getDate(), List.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            List<Map<String,Object>> shift = response.getBody();
            assertEquals(shift.get(0).get("startTime"), shiftSet.getStartTime().toString());
            assertEquals(shift.get(0).get("date"), shiftSet.getDate().toString());
            assertEquals(shift.get(0).get("endTime"), shiftSet.getEndTime().toString());
            assertEquals(shift.get(0).get("shiftId"), shiftSet.getShiftID());
      }
      /**
       * Asserts an error when there are no shifts associated with a date.
       */
      @Test
      @Order(12)
      public void testGetEmptyShiftsByDate() {
            ResponseEntity<String> response = client.getForEntity("/shifts/date/get/" + LocalDate.of(1994,3,06), String.class);
            assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
            assertEquals(response.getBody(),"No shifts found for this date.");
      }

      /**
       * Tests getting valid shifts by date and email
       */
      @Test
      @Order(13)
      public void testGetValidShiftsByDateAndStartTime() {
            ResponseEntity<List> response = client.getForEntity("/shifts/date/st/get/" + shiftSet.getDate() + "/" + shiftSet.getStartTime(), List.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
            List<Map<String,Object>> shift = response.getBody();
            assertEquals(shift.get(0).get("startTime"), shiftSet.getStartTime().toString());
            assertEquals(shift.get(0).get("date"), shiftSet.getDate().toString());
            assertEquals(shift.get(0).get("endTime"), shiftSet.getEndTime().toString());
            assertEquals(shift.get(0).get("shiftId"), shiftSet.getShiftID());
      }
      /**
       * Asserts an error when there are no shifts associated with a date and start time.
       */
      @Test
      @Order(14)
      public void testGetEmptyShiftsByDateAndStartTime() {
            ResponseEntity<String> response = client.getForEntity("/shifts/date/st/get/" + LocalDate.of(1995,6,9) + "/" + Time.valueOf("06:30:00"), String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(response.getBody(), "No shifts found for this date and start time.");
      }
      /*
       * Test that gets all shifts in the system.
       */
      @Test
      @Order(15)
      public void testGetAllShifts() {
            ResponseEntity<List> response = client.getForEntity("/shifts/", List.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(response.getBody().size(), 1);
            List<Map<String, Object>> shifts = response.getBody();

            assertEquals(shiftSet.getDate().toString(), shifts.get(0).get("date"));
            assertEquals(shiftSet.getStartTime().toString(), shifts.get(0).get("startTime"));
            assertEquals(shiftSet.getEndTime().toString(), shifts.get(0).get("endTime"));
            assertEquals(shiftSet.getShiftID(), shifts.get(0).get("shiftId"));
      }
      /*
       * Test that deletes a shift successfully then asserts that it has been deleted.
       */
      @Test
      @Order(16)
      public void testDeleteValidShift() {
            HttpEntity<String> requestEntity = new HttpEntity<>(null);
            ResponseEntity<String> response = client.exchange("/shift/delete/"+shiftSet.getShiftID(), HttpMethod.DELETE, requestEntity, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ResponseEntity<String> testResponse = client.getForEntity("/shift/get/"+shiftSet.getShiftID(), String.class);
            assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
            assertEquals(testResponse.getBody(), "Shift not found.");
      }

      /*
       * Tries deleting a shift with an invalid ID (ID not in system)
       */
      @Test
      @Order(17)
      public void testDeleteInvalidShift() {
            HttpEntity<String> requestEntity = new HttpEntity<>(null);
            ResponseEntity<String> response = client.exchange("/shift/delete/"+shiftSet.getShiftID1(), HttpMethod.DELETE, requestEntity, String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(response.getBody(), "Invalid shift ID.");
      }

}
