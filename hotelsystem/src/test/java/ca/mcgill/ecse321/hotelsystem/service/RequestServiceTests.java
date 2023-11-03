package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.*;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.RequestRepository;
import ca.mcgill.ecse321.hotelsystem.repository.ReservationRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class RequestServiceTests {

    private static final int VALID_REQUEST_KEY = 123;
    private static final CompletionStatus status = CompletionStatus.Pending;
    private static final String REQUEST_DESCRIPTION = "Air humidifier in the room";
    // Reservation mock object data
    private static final int VALID_RESERVATION_ID = 10;
    private static final boolean IS_PAID = true;
    private static final int NUM_PEOPLE = 3;

    @Mock
    private RequestRepository requestDao;

    @Mock
    private ReservationRepository reservationDao;

    @InjectMocks
    private RequestService service;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(reservationDao.findReservationByReservationID(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_RESERVATION_ID)) {
                Reservation res = new Reservation();
                res.setPaid(IS_PAID);
                res.setNumPeople(NUM_PEOPLE);
                return res;
            } else {
                return null;
            }
        });

        lenient().when(requestDao.findRequestByRequestId(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_REQUEST_KEY)) {
                return new Request(status, REQUEST_DESCRIPTION, reservationDao.findReservationByReservationID(VALID_RESERVATION_ID));
            } else {
                return null;
            }
        });
        lenient().when(requestDao.findRequestsByReservation_ReservationID(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VALID_RESERVATION_ID)) {
                return List.of(new Request(CompletionStatus.Pending, REQUEST_DESCRIPTION, reservationDao.findReservationByReservationID(VALID_RESERVATION_ID)));
            } else {
                return List.of();
            }
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(requestDao.save(any(Request.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateValidRequest() {

        Request req = service.createRequest(REQUEST_DESCRIPTION, VALID_RESERVATION_ID);
        assertNotNull(req);
        assertEquals(REQUEST_DESCRIPTION, req.getDescription());
        assertEquals(NUM_PEOPLE, req.getReservation().getNumPeople());
        assertEquals(CompletionStatus.Pending, req.getStatus());
    }

    @Test
    public void testCreateInvalidRequest() {
        HRSException ex = assertThrows(HRSException.class, () -> service.createRequest(null, VALID_RESERVATION_ID));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testCreateRequestForInvalidReservation() {
        HRSException ex = assertThrows(HRSException.class, () -> service.createRequest(REQUEST_DESCRIPTION, VALID_RESERVATION_ID + 1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testGetRequestValid() {
        Request req = service.getRequest(VALID_REQUEST_KEY);
        assertNotNull(req);
        assertEquals(REQUEST_DESCRIPTION, req.getDescription());
        assertEquals(NUM_PEOPLE, req.getReservation().getNumPeople());
    }

    @Test
    public void testGetRequestInvalid() {
        HRSException ex = assertThrows(HRSException.class, () -> service.getRequest(VALID_REQUEST_KEY+1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testGetRequestsForReservation() {
        List<Request> reqs = service.getRequestsForReservationWithId(VALID_RESERVATION_ID);
        assertEquals(1, reqs.size());
        assertEquals(REQUEST_DESCRIPTION, reqs.get(0).getDescription());
    }

    @Test
    public void testGetRequestsForNonexistingReservation() {
        List<Request> reqs = service.getRequestsForReservationWithId(VALID_RESERVATION_ID+1);
        assertEquals(0, reqs.size());
    }

    @Test
    public void testChangeRequestValidStatus() {
        Request req = service.changeRequestStatus(VALID_REQUEST_KEY, CompletionStatus.Done);
        assertNotNull(req);
        assertEquals(CompletionStatus.Done, req.getStatus());
    }

    @Test
    public void testChangeRequestInvalidStatus() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRequestStatus(VALID_REQUEST_KEY, null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    public void testChangeInvalidRequestStatus() {
        HRSException ex = assertThrows(HRSException.class, () -> service.changeRequestStatus(VALID_REQUEST_KEY+1, CompletionStatus.InProgress));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    public void testDeleteInvalidRequest() {
        HRSException ex = assertThrows(HRSException.class, () -> service.deleteRequest(VALID_REQUEST_KEY+ 1));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

}
