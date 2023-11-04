package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Employee;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.EmployeeService;
import ca.mcgill.ecse321.hotelsystem.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import ca.mcgill.ecse321.hotelsystem.service.ShiftService;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
@CrossOrigin(origins="*")
@RestController
public class ShiftController {
      // shift request --> post (fill a form)
      // shift response --> get (click on link)
      @Autowired
      private ShiftService shiftService;

      @Autowired
      private EmployeeService employeeService;

      @PostMapping("/shift/create")
      public ResponseEntity<ShiftResponseDto> createShift(@RequestBody ShiftRequestDto shiftToCreate) {
            Shift shift;
            if (shiftToCreate.getEmployeeEmail() == null) {
                  shift = shiftToCreate.toModel(null);
            } else {
                  shift = shiftToCreate.toModel(employeeService.getEmployeeByEmail(shiftToCreate.getEmployeeEmail()));
            }
            shift = shiftService.createShift(shift);
            return new ResponseEntity<ShiftResponseDto>(new ShiftResponseDto(shift), HttpStatus.CREATED);
      }

      @GetMapping("/shifts")
      public List<ShiftResponseDto> getAllShifts() {
            Iterable<Shift> allShifts = shiftService.getAllShifts();
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: allShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      @GetMapping("/shift/{shiftID}")
      public ShiftResponseDto getShiftByID(@PathVariable int shiftID) {
            Shift shift = shiftService.getShiftByShiftID(shiftID);
            return new ShiftResponseDto(shift);
      }

      @GetMapping("/shifts/{employeeEmail}")
      public List<ShiftResponseDto> getShiftsByEmail(@PathVariable String employeeEmail) {
            Iterable<Shift> employeeShifts = shiftService.getShiftsByEmployeeEmail(employeeEmail);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: employeeShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      @GetMapping("/shifts/{date}")
      public List<ShiftResponseDto> getShiftsByDate(@PathVariable LocalDate date) {
            Iterable<Shift> dateShifts = shiftService.getShiftsByDate(date);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: dateShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      @GetMapping("/shifts/{date},{startTime}")
      public List<ShiftResponseDto> getShiftsByDateAndStartTime(@PathVariable LocalDate date, @PathVariable Time startTime) {
            Iterable<Shift> dtShifts = shiftService.getShiftsByDateAndStartTime(date,startTime);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: dtShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      // write update controller method
      @PutMapping("/shift/update")
      public ResponseEntity<ShiftResponseDto> updateShift(@PathVariable ShiftRequestDto shiftToUpdate) {
            Shift shiftModel = shiftToUpdate.toModel(employeeService.getEmployeeByEmail(shiftToUpdate.getEmployeeEmail()));
            Shift updatedShift = shiftService.updateShift(shiftModel);
            ShiftResponseDto responseDto = new ShiftResponseDto(updatedShift);
            return new ResponseEntity<ShiftResponseDto>(responseDto, HttpStatus.OK);
      }

      @DeleteMapping("/shift/delete")
      public void deleteShift(@RequestBody ShiftRequestDto shiftToDelete) {
            Shift shiftModel = shiftToDelete.toModel(employeeService.getEmployeeByEmail(shiftToDelete.getEmployeeEmail()));
            shiftService.deleteShift(shiftModel);
      }

}
