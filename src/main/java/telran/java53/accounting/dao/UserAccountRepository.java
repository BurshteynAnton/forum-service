package telran.java53.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java53.accounting.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {



}
