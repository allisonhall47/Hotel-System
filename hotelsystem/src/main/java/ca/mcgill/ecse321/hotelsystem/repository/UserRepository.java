package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

    User findUserByEmail(String email);
    List<User> findUsersByName(String name);
    List<User> findAll();
    void deleteUserByEmail(String email);

}
