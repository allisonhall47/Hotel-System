package ca.mcgill.ecse321.hotelsystem.repository;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    Account findAccountByAccountNumber(int accountNumber);
    void deleteAccountByAccountNumber(int accountNumber);

}
