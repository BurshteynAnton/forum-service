package java53.forumservice.accounting.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "login")
@NoArgsConstructor
@Document(collection = "users")

public class User {
    String login;
    @Setter
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    LocalDateTime dateCreated = LocalDateTime.now();
    private Set<String> roles = new HashSet<>();

    public User(String login, String firstName,
                String lastName, Set<String> roles) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
}
