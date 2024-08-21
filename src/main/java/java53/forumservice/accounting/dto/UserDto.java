package java53.forumservice.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    String Login;
    String firstName;
    String lastName;
    @Singular
    Set<String> roles;


}
