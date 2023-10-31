package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.service.EmployeeService;
import ca.mcgill.ecse321.hotelsystem.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShiftController {
      // shift request --> post (fill a form)
      // shift response --> get (click on link)
      @Autowired
      ShiftService shiftService;

      @Autowired
      EmployeeService employeeService;



}
