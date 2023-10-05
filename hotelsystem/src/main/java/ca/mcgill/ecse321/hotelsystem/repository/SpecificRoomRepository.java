package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecificRoomRepository extends CrudRepository<SpecificRoom, Integer> {
    SpecificRoom findSpecificRoomByNumber(int number);
    void deleteByNumber(int number);
}
