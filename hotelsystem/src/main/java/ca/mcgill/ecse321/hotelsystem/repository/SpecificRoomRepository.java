package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.SpecificRoom;
import ca.mcgill.ecse321.hotelsystem.Model.ViewType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecificRoomRepository extends CrudRepository<SpecificRoom, Integer> {
    SpecificRoom findSpecificRoomByNumber(int number);
    List<SpecificRoom> findSpecificRoomsByView(ViewType view);
    List<SpecificRoom> findSpecificRoomsByRoom_Type(String type);
    List<SpecificRoom> findSpecificRoomsByOpenForUseIsTrue();
    List<SpecificRoom> findSpecificRoomsByOpenForUseIsFalse();

    List<SpecificRoom> findAll();
    void deleteByNumber(int number);

}
