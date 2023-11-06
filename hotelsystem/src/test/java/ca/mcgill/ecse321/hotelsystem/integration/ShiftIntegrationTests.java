package ca.mcgill.ecse321.hotelsystem.integration;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.hotelsystem.repository.EmployeeRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ShiftRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ShiftIntegrationTests {

      private class ShiftSet {

            public LocalDate date = LocalDate.of(1995, 6,7);
            public Time startTime = Time.valueOf("07:30:00");
            public Time endTime = Time.valueOf("09:30:00");

            public String getEmployeeEmail() {
                  return employeeEmail;
            }

            public void setEmployeeEmail(String employeeEmail) {
                  this.employeeEmail = employeeEmail;
            }

            private String employeeEmail;

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

      @Autowired
      private TestRestTemplate client;
      @Autowired
      private ShiftRepository shiftRepository;

      @Autowired
      private EmployeeRepository EmployeeRepository;


      @BeforeAll
      public void setupShiftSet() {
            this.shiftSet = new ShiftSet();
      }
      @AfterAll
      public void clearDatabase() {
            shiftRepository.deleteAll();
      }

      @Test
      @Order(0)
      public void testGetAllEmptyShifts() {
            ResponseEntity<String> response = client.getForEntity("/shifts", String.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(response.getBody(), "There are no shifts in the system.");
      }

      @Test
      @Order(1)
      public void testCreateValidShift() {
            ShiftRequestDto shiftRequest = new ShiftRequestDto(shiftSet.startTime,shiftSet.endTime,shiftSet.date);
            ResponseEntity<ShiftResponseDto> shiftResponse = client.postForEntity("/shift/create", shiftRequest, ShiftResponseDto.class);

            assertEquals(HttpStatus.CREATED,shiftResponse.getStatusCode());
            assertNotNull(shiftResponse.getBody());
            assertTrue(equals(shiftResponse.getBody(), shiftSet)); // right now, this is false
            shiftSet.setEmployeeEmail(shiftResponse.getBody().getEmployeeEmail());
      }

      private boolean equals(ShiftResponseDto response, ShiftSet s) {
            boolean a = response.getDate().equals(s.getDate());
            boolean b = response.getStartTime().equals(s.getStartTime());
            boolean c = response.getEndTime().equals(s.getEndTime());
            return (a && b && c);
      }

      @Test
      @Order(2)
      public void testDeleteValidShift() {
            // TO DO
      }

}
