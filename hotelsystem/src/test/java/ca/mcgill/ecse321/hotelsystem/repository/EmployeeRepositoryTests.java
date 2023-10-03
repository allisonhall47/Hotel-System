package ca.mcgill.ecse321.hotelsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadPerson() {
        // Create person.
//        String name = "Muffin Man";
//        Integer age = 40;
//        String address = "123 Drury Lane";
//        Person person = new Person();
//        person.setName(name);
//        person.setAge(age);
//        person.setAddress(address);
//
//        // Save person
//        personRepository.save(person);
//
//        // Read person from database.
//        person = personRepository.findPersonByName(name);
//
//        // Assert that person is not null and has correct attributes.
//        assertNotNull(person);
//        assertEquals(name, person.getName());
//        assertEquals(age, person.getAge());
//        assertEquals(address, person.getAddress());
    }
}