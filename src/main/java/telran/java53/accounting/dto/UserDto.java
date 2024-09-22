package telran.java53.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import telran.java53.accounting.model.Role;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	String login;
    String firstName;
    String lastName;
    String email;
    @Singular
    Set<Role> roles;
}
