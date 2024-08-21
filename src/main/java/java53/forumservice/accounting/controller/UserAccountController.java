package java53.forumservice.accounting.controller;

import java53.forumservice.accounting.dto.RolesDto;
import java53.forumservice.accounting.dto.UserDto;
import java53.forumservice.accounting.dto.UserEditDto;
import java53.forumservice.accounting.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserAccountController {

    @PostMapping("/user/{account}")
    public UserDto register(UserRegisterDto userRegisterDto) {
        //TODO
        return null;
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(String login) {
        return userAccountService.removeUser(login);
    }

    @DeleteMapping("/user/{login}")
    public UserDto removeUser(@PathVariable String login) {

    }

    @PutMapping("/user/{login}")
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        return null;
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        return null;
    }


    public void changePassword(String login, String newPassword) {
    //TODO
    }
}
