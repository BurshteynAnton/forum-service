package java53.forumservice.accounting.dao;

import java53.forumservice.accounting.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserAccountRepository extends MongoRepository<User, String> {


    Optional<User> findByLogin(String name);
}
