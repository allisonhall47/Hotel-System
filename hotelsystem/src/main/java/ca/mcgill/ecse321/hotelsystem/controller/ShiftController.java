package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.dto.ShiftRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.EmployeeService;
import ca.mcgill.ecse321.hotelsystem.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.hotelsystem.Model.Shift;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="*")
@RestController
public class ShiftController {
      @Autowired
      private ShiftService shiftService;

      @Autowired
      private EmployeeService employeeService;

      /**
       * createShift: creates a shift
       * @param shiftToCreate : shift to be created
       * @return response entity containing the response dto object
       */
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

      /**
       * getAllShifts: gets all shifts
       * @return list of all shift response objects
       */
      @GetMapping("/shifts/")
      public List<ShiftResponseDto> getAllShifts() {
            Iterable<Shift> allShifts = shiftService.getAllShifts();
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: allShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      /**
       * getShiftByID : gets shift by shiftID
       * @param shiftID : shift ID of shift that is being retrieved
       * @return response object of shift
       */
      @GetMapping("/shift/get/{shiftID}")
      public ShiftResponseDto getShiftByID(@PathVariable int shiftID) {
            Shift shift = shiftService.getShiftByShiftID(shiftID);
            return new ShiftResponseDto(shift);
      }

      /**
       * getShiftsByEmail: gets list of shifts by employee email
       * @param employeeEmail : email of employee for whose shifts are being retrieved
       * @return list of corresponding shift response objects
       */
      @GetMapping("/shifts/get/{employeeEmail}")
      public List<ShiftResponseDto> getShiftsByEmail(@PathVariable String employeeEmail) {
            Iterable<Shift> employeeShifts = shiftService.getShiftsByEmployeeEmail(employeeEmail);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: employeeShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      /**
       * getShiftsByDate :  get list of shifts for a date
       * @param date : date of the shifts we want to retrieve
       * @return list of corresponding shift response objects
       */
      @GetMapping("/shifts/date/get/{date}")
      public List<ShiftResponseDto> getShiftsByDate(@PathVariable LocalDate date) {
            Iterable<Shift> dateShifts = shiftService.getShiftsByDate(date);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: dateShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      /**
       * getShiftsByDateAndStartTime : get list of shifts of a certain date and start time
       * @param date : date of shifts of retrieval
       * @param startTime : start time of shifts of retrieval
       * @return list of corresponding shift response objects
       */
      @GetMapping("/shifts/date/st/get/{date}/{startTime}")
      public List<ShiftResponseDto> getShiftsByDateAndStartTime(@PathVariable LocalDate date, @PathVariable Time startTime) {
            Iterable<Shift> dtShifts = shiftService.getShiftsByDateAndStartTime(date,startTime);
            List<ShiftResponseDto> dtos = new ArrayList<ShiftResponseDto>();
            for (Shift s: dtShifts) {
                  dtos.add(new ShiftResponseDto(s));
            }
            return dtos;
      }

      /**
       * updateShift : updates a shift
       * @param shiftID : old shift
       * @param shiftForUpdate: new shift
       * @return response entity containing the shift response object
       */
      @PutMapping(value = {"/shift/{shiftID}", "/shift/{shiftID}/"})
      public ResponseEntity<ShiftResponseDto> updateShift(@PathVariable int shiftID, @RequestBody ShiftRequestDto shiftForUpdate) {
           Shift shift = shiftForUpdate.toModel(shiftService.getShiftByShiftID(shiftID).getEmployee());
           shift = shiftService.updateShift(shift, shiftID);
           return new ResponseEntity<ShiftResponseDto>(new ShiftResponseDto(shift), HttpStatus.OK);
      }

      /**
       * deleteShift : deletes a shift
       * @param shiftID : ID of shift that needs deletion
       */
      @DeleteMapping(value = "/shift/delete/{shiftID}")
      public void deleteShift(@PathVariable int shiftID) {
            shiftService.deleteShift(shiftID);
      }

}
