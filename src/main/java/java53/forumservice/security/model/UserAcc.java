package java53.forumservice.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.security.Principal;
import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
public class UserAcc implements Principal {
    private String name;
    @Singular
    private Set<String> roles;

}
