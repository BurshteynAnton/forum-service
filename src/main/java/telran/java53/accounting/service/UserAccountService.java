package telran.java53.accounting.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import telran.java53.accounting.dto.RolesDto;
import telran.java53.accounting.dto.UserDto;
import telran.java53.accounting.dto.UserEditDto;
import telran.java53.accounting.dto.UserRegisterDto;
import telran.java53.accounting.model.UserAccount;

public interface UserAccountService extends UserDetailsService {
	UserDto register(UserRegisterDto userRegisterDto);

	UserDto getUser(String login);

	UserDto removeUser(String login);

	UserDto updateUser(String login, UserEditDto userEditDto);

	RolesDto changeRolesList(String login, String role, boolean isAddRoles);

	void changePassword(String login, String newPassword);

	String signUpUser(UserAccount userAccount);

	UserDto updateUserAccountStatus(String login, boolean enabled);

}
