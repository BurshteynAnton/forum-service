package java53.forumservice.accounting.dto;

import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class RolesDto {
    String Login;
    @Singular
    Set<String> Roles;
}
