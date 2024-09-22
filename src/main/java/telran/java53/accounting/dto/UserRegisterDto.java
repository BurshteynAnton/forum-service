package telran.java53.accounting.dto;

import lombok.*;
import telran.java53.accounting.model.Role;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class UserRegisterDto extends UserDto {
    String password;
}
