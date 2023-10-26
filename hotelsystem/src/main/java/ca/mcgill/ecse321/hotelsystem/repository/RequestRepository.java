package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Integer> {
    Request findRequestByRequestId(int id);
    List<Request> findRequestsByReservation_ReservationID(int id);
    void deleteRequestByRequestId(int id);
    void deleteRequestsByReservation_ReservationID(int id);
    List<Request> findRequestsByStatus(CompletionStatus status);

    List<Request> findAll();
    

}
