package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Shift;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface ShiftRepository extends CrudRepository<Shift, Integer>{

    Shift findShiftByShiftId(int shiftId);
    Shift findShiftByDate(Date date);
    Shift findShiftByStartTime(Time startTime);
    List<Shift> findShiftsByEmployeeEmail(String email); // potential Employee_email
    void deleteShiftByShiftId(int shiftId);


}
