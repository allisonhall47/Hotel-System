package ca.mcgill.ecse321.hotelsystem.controller;

import ca.mcgill.ecse321.hotelsystem.Model.Customer;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationRequestDto;
import ca.mcgill.ecse321.hotelsystem.dto.ReservationResponseDto;
import ca.mcgill.ecse321.hotelsystem.service.CustomerService;
import ca.mcgill.ecse321.hotelsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="*")
@RestController //@Controller + @ResponseBody
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    CustomerService customerService;

    /**
     * create a new reservation
     * @param reservation info about the new reservation
     * @return dto of the new object
     */
    @PostMapping("/reservation/new")
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto reservation) {
        Customer customer = customerService.getCustomerByEmail(reservation.getCustomerEmail());
        Reservation res = reservation.toModel(customer);
        res = reservationService.createReservation(res);
        return new ReservationResponseDto(res);
    }

    /**
     * get all reservations
     * @return list of dto reservation objects
     */
    @GetMapping("/reservation")
    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> list = reservationService.getAllReservations();
        List<ReservationResponseDto> dtos = new ArrayList<>();
        for(Reservation res: list) {
            dtos.add(new ReservationResponseDto(res));
        }
        return dtos;
    }

    /**
     * get non Paid reservations
     * @return list of dto objects
     */
    @GetMapping("/reservation/not-paid")
    public List<ReservationResponseDto> getAllReservationsNotPaid() {
        List<Reservation> list = reservationService.getReservationsNotPaid();
        List<ReservationResponseDto> dtos = new ArrayList<>();
        for(Reservation res: list) {
            dtos.add(new ReservationResponseDto(res));
        }
        return dtos;
    }

    /**
     * get reservation by id
     * @param id id of res
     * @return dto object
     */
    @GetMapping("/reservation/{reservationId}")
    public ReservationResponseDto getReservationById(@PathVariable(value = "reservationId") int id) {
        Reservation res = reservationService.getReservation(id);
        return new ReservationResponseDto(res);
    }

    /**
     * check in for a reservation
     * @param id id
     * @return updated dto object
     */
    @PutMapping ("/reservation/{reservationId}/checkIn")
    public ReservationResponseDto checkInReservationById(@PathVariable(value = "reservationId") int id) {
        Reservation res = reservationService.getReservation(id);
        return new ReservationResponseDto(reservationService.checkIn(res));
    }

    @PutMapping ("/reservation/{reservationId}/checkIn")
    public ReservationResponseDto checkOutReservationById(@PathVariable(value = "reservationId") int id) {
        Reservation res = reservationService.getReservation(id);
        return new ReservationResponseDto(reservationService.checkOut(res));
    }

    /**
     * get all reservations for customer
     * @param customerEmail email
     * @return list of dto objects
     */
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

    /**
     * delete reservation with id
     * @param id id
     */
    @DeleteMapping("/reservation/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") int id) {
        Reservation reservation = reservationService.getReservation(id);
        reservationService.deleteReservation(reservation);
    }

    /**
     * Pay reservation
     * @param id id
     * @param money amount of money
     * @return updated dto object
     */
    @PutMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationResponseDto> payReservation(@PathVariable("reservationId") int id, @RequestParam int money) {
        Reservation reservation = reservationService.getReservation(id);
        Reservation newRes = reservationService.payReservation(reservation, money);
        return new ResponseEntity<>(new ReservationResponseDto(newRes), HttpStatus.OK);
    }

}
