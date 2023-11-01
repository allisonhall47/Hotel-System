package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.CustomerService;
import ca.mcgill.ecse321.hotelsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController //@Controller + @ResponseBody
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/reservation/customer/{customerEmail}/new")
    public ReservationResponseDto createReservation(@PathVariable("customerEmail") String customerEmail, @RequestBody ReservationRequestDto reservation) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        Reservation res = reservation.toModel(customer);
        res = reservationService.createReservation(res);
        return new ReservationResponseDto(res);
    }

    @GetMapping("/reservation")
    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> list = reservationService.getAllReservations();
        List<ReservationResponseDto> dtos = new ArrayList<>();
        for(Reservation res: list) {
            dtos.add(new ReservationResponseDto(res));
        }
        return dtos;
    }

    @GetMapping("/reservation/{reservationId}")
    public ReservationResponseDto getReservationById(@PathVariable(value = "reservationId") int id) {
        return new ReservationResponseDto(reservationService.getReservation(id));
    }

    @GetMapping("/reservation/customer/{customerEmail}")
    public List<ReservationResponseDto> getAllReservationsCustomer(@PathVariable(value = "customerEmail") String customerEmail) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        List<Reservation> list = reservationService.getReservationsByCustomer(customer);
        List<ReservationResponseDto> dtos = new ArrayList<>();
        for(Reservation res: list) {
            dtos.add(new ReservationResponseDto(res));
        }
        return dtos;
    }

    //TODO idk wtf im doing here, review
    @DeleteMapping("/reservation/{reservationId}")
    public String deleteReservation(@PathVariable("reservationId") int id) {
        Reservation reservation = reservationService.getReservation(id);
        reservationService.deleteReservation(reservation);
        return "redirect:/reservation";
    }

    @PutMapping("/reservation/{reservationId}")
    public ResponseEntity<Reservation> payReservation(@PathVariable("reservationId") int id, @RequestBody int money) {
        Reservation reservation = reservationService.getReservation(id);
        Reservation newRes = reservationService.payReservation(reservation, money);
        return new ResponseEntity<Reservation>(newRes, HttpStatus.ACCEPTED);
    }

}
