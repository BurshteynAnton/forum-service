package telran.java53.accounting.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import telran.java53.accounting.model.Role;
import telran.java53.accounting.model.UserAccount;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    Optional<UserAccount> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserAccount u " + "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableUserAccount(String email);

    @Modifying
    @Query("UPDATE UserAccount u SET u.roles = :roles WHERE :role NOT MEMBER OF u.roles")
    void addRoleToAllUsersIfNotExists(@Param("role") Role role, @Param("roles") Set<Role> roles);

}