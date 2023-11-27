package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Repair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepairRepository extends CrudRepository<Repair, Integer> {
    Repair findRepairByRepairId(int repairId);
    List<Repair> findRepairsByEmployee_Email(String email);
    void deleteRepairByRepairId(int repairId);

    List<Repair> findAll();

}