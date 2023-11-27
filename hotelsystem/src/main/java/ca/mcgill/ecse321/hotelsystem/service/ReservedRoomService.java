package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import ca.mcgill.ecse321.hotelsystem.Model.ReservedRoom;
import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservedRoomRepository;
import ca.mcgill.ecse321.hotelsystem.repository.SpecificRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservedRoomService {

    @Autowired
    ReservedRoomRepository reservedRoomRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    SpecificRoomService specificRoomService;

    @Autowired
    ReservationRepository reservationRepository;
//
//    @Autowired
//    SpecificRoomRepository specificRoomRepository;

    @Transactional
    public ReservedRoom createReservedRoom(ReservedRoom reservedRoom) {
        //input check in other services
        Reservation reservation = reservedRoom.getReservation();
        reservedRoom.setReservation(null); //temporarily sent to null
        //reservedRoom = reservedRoomRepository.save(reservedRoom);

        reservedRoom = this.assignReservedRoomToReservation(reservation,reservedRoom); //this to check if valid
        //set the price for the reservation
        reservation = reservedRoom.getReservation();
        //TODO check if it works
        long days = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        reservation.setTotalPrice((reservedRoom.getSpecificRoom().getRoom().getRate()*(int)days) + reservation.getTotalPrice()); //add additional price to the initial one
        reservationRepository.save(reservation);
        reservedRoom.setReservation(reservation);
        reservedRoom = reservedRoomRepository.save(reservedRoom);

        return reservedRoom;
    }

    /**
     * GetAllReservedRooms: service method to fetch all existing reserved rooms in the database
     * @return List of reserved rooms
     * @throws HRSException if no reserved rooms exist in the system
     */
    @Transactional
    public List<ReservedRoom> getAllReservedRooms(){
        return reservedRoomRepository.findAll();
    }

    /**
     * getReservedRoomById: service method to get reserved room with the id
     * @param id unique id
     * @return reserved room
     */
    @Transactional
    public ReservedRoom getReservedRoomById(int id) {
        ReservedRoom room = reservedRoomRepository.findReservedRoomByReservedID(id);
        if(room == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservedRoom with id does not exist");
        }
        return room;
    }

    /**
     * getReservedRoomsByReservation: service method to get reserved rooms booked in a reservation
     * @param reservationId reservation id
     * @return list of reserved rooms
     */
    @Transactional
    public List<ReservedRoom> getReservedRoomsByReservation(int reservationId) {
        List<ReservedRoom> list = reservedRoomRepository.findReservedRoomsByReservation_ReservationID(reservationId);
        if(list == null || list.isEmpty()) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reserved room for given reservation with id: " + reservationId);
        }
        return list;
    }

    /**
     * getReservedRoomsBySpecRoom: service method to get reserved rooms that are linked to a specific room number
     * @param specRoom specific room to find linked reserved rooms
     * @return list of reserved rooms
     */
    @Transactional
    public List<ReservedRoom> getReservedRoomsBySpecRoom(SpecificRoom specRoom) {
        if(specRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "specific room does not exist");
        }
        return reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber());
    }

    /**
     * deleteReservedRoom: service method to delete a reserved room
     * @param reservedRoom reserved room to be deleted
     */
    @Transactional
    public void deleteReservedRoom(ReservedRoom reservedRoom) {
        reservedRoom = reservedRoomRepository.findReservedRoomByReservedID(reservedRoom.getReservedID());
        if(reservedRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reserved room with id doesn't exist");
        }
        reservedRoomRepository.delete(reservedRoom);
    }

    /**
     * assignReservedRoomToReservation: service method to assign a room to a reservation
     * @param reservation reservation to assign to
     * @param room reserved room to assign
     * @return the reservedRoom
     */
    @Transactional
    public ReservedRoom assignReservedRoomToReservation(Reservation reservation, ReservedRoom room) {
        //2 reserved rooms cant be reserved at the same time, when assigning a room to a reservation
        //get all reservations for specRoom and check checkIn and checkOut dates to make sure no overlap for the same room
        SpecificRoom specRoom = room.getSpecificRoom(); //TODO assume specRoom is not null and is already assigned to reservedRoom
        //get reservedRooms for specRoom, and check the dates
        List<ReservedRoom> list = reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber());
        for(ReservedRoom resRoom : list) {
            Reservation res = resRoom.getReservation();
            if(res == null) continue; //if res null, then skip no need to check
            LocalDate checkIn = res.getCheckIn();
            LocalDate checkOut = res.getCheckOut();
            if((reservation.getCheckIn().isBefore(checkOut) && reservation.getCheckIn().isAfter(checkIn)) ||
                    (reservation.getCheckOut().isBefore(checkOut) && reservation.getCheckOut().isAfter(checkIn)) ||
                    (reservation.getCheckIn().isBefore(checkOut) && reservation.getCheckOut().isAfter(checkIn))) {
                throw new HRSException(HttpStatus.CONFLICT, "a reservation with conflicting check-in and check-out dates exists");
            }
        }
        room.setReservation(reservation);
        return reservedRoomRepository.save(room);
    }
}
