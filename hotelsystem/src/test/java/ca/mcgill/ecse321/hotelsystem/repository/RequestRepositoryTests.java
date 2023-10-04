package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.CompletionStatus;
import ca.mcgill.ecse321.hotelsystem.Model.Request;
import ca.mcgill.ecse321.hotelsystem.Model.Reservation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RequestRepositoryTests {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @AfterEach
    public void clearDatabase() {
        requestRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadRequest() {
        // Create request.
        String description = "Need more towels";
        CompletionStatus status = CompletionStatus.Pending;
        RequestAndResId requestAndResId = createRequest(null, description, status);
        Request request = requestAndResId.req;



        request = requestRepository.save(request);

        request = requestRepository.findRequestByRequestId(request.getRequestId());

        assertNotNull(request);
        assertEquals(description, request.getDescription());
        assertEquals(status, request.getStatus());
        assertNotNull(request.getReservation());
        assertEquals(requestAndResId.resId, request.getReservation().getReservationID());
    }

    @Test
    public void testFindByStatus() {
        String descr1 = "Need more towels";
        CompletionStatus status1 = CompletionStatus.Pending;
        createRequest(null, descr1, status1);
        String descr2 = "Want a coffee machine";
        CompletionStatus status2 = CompletionStatus.Done;
        createRequest(null, descr2, status2);
        String descr3 = "Please clean room";
        CompletionStatus status3 = CompletionStatus.Pending;
        createRequest(null, descr3, status3);

        assertEquals(2, requestRepository.findRequestsByStatus(CompletionStatus.Pending).size());
        assertEquals(1, requestRepository.findRequestsByStatus(CompletionStatus.Done).size());
        assertEquals(0, requestRepository.findRequestsByStatus(CompletionStatus.InProgress).size());

        Request req2 = requestRepository.findRequestsByStatus(CompletionStatus.Done).get(0);
        assertEquals(descr2, req2.getDescription());
        assertEquals(status2, req2.getStatus());
    }

    @Test
    public void testFindByReservation() {
        Reservation res12 = new Reservation();
        res12 = reservationRepository.save(res12);

        String descr1 = "Need more towels";
        CompletionStatus status1 = CompletionStatus.Pending;
        createRequest(res12, descr1, status1);
        String descr2 = "Want a coffee machine";
        CompletionStatus status2 = CompletionStatus.Done;
        createRequest(res12, descr2, status2);
        String descr3 = "Please clean room";
        CompletionStatus status3 = CompletionStatus.Pending;
        RequestAndResId requestAndResId3 = createRequest(null, descr3, status3);

        assertEquals(2, requestRepository.findRequestsByReservation_ReservationID(res12.getReservationID()).size());
        assertEquals(1, requestRepository.findRequestsByReservation_ReservationID(requestAndResId3.resId).size());
        Request req3 = requestRepository.findRequestsByReservation_ReservationID(requestAndResId3.resId).get(0);
        assertEquals(descr3, req3.getDescription());
        assertEquals(status3, req3.getStatus());
    }

    public void testDeleteById() {
        String description = "Need more towels";
        CompletionStatus status = CompletionStatus.Pending;
        RequestAndResId requestAndResId = createRequest(null, description, status);

        requestRepository.deleteRequestByRequestId(requestAndResId.req.getRequestId());
        assertFalse(requestRepository.findAll().iterator().hasNext(), "Request table should be empty");
    }


    private RequestAndResId createRequest(Reservation reservation, String description, CompletionStatus status) {

        if (reservation == null) {
            reservation = new Reservation();
            reservation = reservationRepository.save(reservation);
        }
        int reservationId = reservation.getReservationID();

        Request request = new Request();

        request.setReservation(reservation);
        request.setDescription(description);
        request.setStatus(status);

        request = requestRepository.save(request);

        return new RequestAndResId(request, reservationId);
    }

    private record RequestAndResId(Request req, int resId) {}
}
