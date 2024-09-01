package java53.forumservice.accounting.service;

import java53.forumservice.accounting.dto.RolesDto;
import java53.forumservice.accounting.dto.UserDto;
import java53.forumservice.accounting.dto.UserEditDto;
import java53.forumservice.accounting.dto.UserRegisterDto;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto getUser(String login);

    UserDto removeUser(String login);

    UserDto updateUser(String login, UserEditDto userEditDto);

    RolesDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword(String login, String newPassword);

    void run(String... args) throws Exception;
}
