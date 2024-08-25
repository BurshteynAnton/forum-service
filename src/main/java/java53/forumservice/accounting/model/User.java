package java53.forumservice.accounting.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")

public class User {
    @Id
    String login;
    @Setter
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    LocalDateTime dateCreated = LocalDateTime.now();
    private Set<Role> roles = new HashSet<>();

    //for Model Mapper
    public User() {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public User(String login, String firstName,
                String lastName, String password) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public boolean addRole(String role) {
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role) {
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}
