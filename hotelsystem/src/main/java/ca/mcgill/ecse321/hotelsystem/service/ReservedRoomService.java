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
import java.util.List;

@Service
public class ReservedRoomService {

    @Autowired
    ReservedRoomRepository reservedRoomRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SpecificRoomRepository specificRoomRepository;

    @Transactional
    public ReservedRoom createReservedRoom(SpecificRoom specRoom, Reservation reservation) {
        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
        if(reservation == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        specRoom = specificRoomRepository.findSpecificRoomByNumber(specRoom.getNumber());
        if(specRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }

        ReservedRoom room = new ReservedRoom(reservation, null);
        room = reservedRoomRepository.save(room);
        room = this.assignReservedRoomToReservation(reservation,room);
        return room;
    }

    /**
     * GetAllReservedRooms: service method to fetch all existing reserved rooms in the database
     * @return List of reserved rooms
     * @throws HRSException if no reserved rooms exist in the system
     */
    @Transactional
    public List<ReservedRoom> getAllReservedRooms(){
        List<ReservedRoom> reservedRooms = reservedRoomRepository.findAll();
        if (reservedRooms == null || reservedRooms.isEmpty()){
            throw new HRSException(HttpStatus.NOT_FOUND, "There are no reserved rooms in the system.");
        }
        return reservedRooms;
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
     * @param reservation reservation
     * @return list of reserved rooms
     */
    @Transactional
    public List<ReservedRoom> getReservedRoomsByReservation(Reservation reservation) {
        List<ReservedRoom> list = reservedRoomRepository.findReservedRoomsByReservation_ReservationID(reservation.getReservationID());
        if(list == null || list.isEmpty()) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reserved room for given reservation with id: " + reservation.getReservationID());
        }
        return list;
    }

    /**
     * getReservedRoomsBySpecRoom: service method to get reserved rooms that are linked to a specific room number
     * @param specRoom specific room
     * @return list of reserved rooms
     */
    @Transactional
    public List<ReservedRoom> getReservedRoomsBySpecRoom(SpecificRoom specRoom) {
        specRoom = specificRoomRepository.findSpecificRoomByNumber(specRoom.getNumber());
        if(specRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
        }
        List<ReservedRoom> list = reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber());
        if(list == null || list.isEmpty()) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reserved room for given specific room with number: " + specRoom.getNumber());
        }
        return list;
    }

    /**
     * deleteReservedRoom: service method to delete a reserved room
     * @param reservedRoom reserved room to be deleted
     */
    @Transactional
    public void deleteReservedRoom(ReservedRoom reservedRoom) {
        int id = reservedRoom.getReservedID();
        reservedRoom = reservedRoomRepository.findReservedRoomByReservedID(id);
        if(reservedRoom == null) {
            throw new HRSException(HttpStatus.NOT_FOUND, "no reserved room with id: " + id);
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
        //TODO no need to check input??
//        reservation = reservationRepository.findReservationByReservationID(reservation.getReservationID());
//        if(reservation == null) {
//            throw new HRSException(HttpStatus.NOT_FOUND, "reservation does not exist");
//        }
//        room = reservedRoomRepository.findReservedRoomByReservedID(room.getReservedID());
//        if(room == null) {
//            throw new HRSException(HttpStatus.NOT_FOUND, "reservedRoom with id does not exist");
//        }

        //get all reservations for specRoom and check checkIn and checkOut dates to make sure no overlap for the same room
        SpecificRoom specRoom = room.getSpecificRoom(); //TODO assume specRoom is not null and is already assigned to reservedRoom
        //get reservedRooms for specRoom, and check the dates
        List<ReservedRoom> list = reservedRoomRepository.findReservedRoomsBySpecificRoom_Number(specRoom.getNumber());
        for(ReservedRoom resRoom : list) {
            Reservation res = resRoom.getReservation();
            if(res == null) continue;
            Date checkIn = res.getCheckIn();
            Date checkOut = res.getCheckOut();
            if((reservation.getCheckIn().before(checkOut) && reservation.getCheckIn().after(checkIn)) ||
                    (reservation.getCheckOut().before(checkOut) && reservation.getCheckOut().after(checkIn))) {
                throw new HRSException(HttpStatus.CONFLICT, "a reservation with conflicting check-in and check-out dates exists");
            }
        }

        room.setReservation(reservation);
        return reservedRoomRepository.save(room);
    }
}
