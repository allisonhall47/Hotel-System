package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift, Integer>{

    Shift findShiftByShiftId(int shiftId);
    List<Shift> findShiftsByDate(LocalDate date);
    List<Shift> findShiftsByDateAndStartTime(LocalDate date, Time startTime);
    List<Shift> findShiftsByEmployeeEmail(String email);
    void deleteShiftByShiftId(int shiftId);

    List<Shift> findAll();


}
