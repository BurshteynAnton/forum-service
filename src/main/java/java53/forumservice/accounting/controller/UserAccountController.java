package java53.forumservice.accounting.controller;

import java53.forumservice.accounting.dto.RolesDto;
import java53.forumservice.accounting.dto.UserDto;
import java53.forumservice.accounting.dto.UserEditDto;
import java53.forumservice.accounting.dto.UserRegisterDto;
import java53.forumservice.accounting.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController {
    final UserAccountService userAccountService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userAccountService.register(userRegisterDto);
    }

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return userAccountService.getUser(principal.getName());
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userAccountService.getUser(login);
    }

    @DeleteMapping("/user/{login}")
    public UserDto removeUser(@PathVariable String login) {
    return userAccountService.removeUser(login);
    }

    @PutMapping("/user/{login}")
    public UserDto updateUser(@PathVariable String login, UserEditDto userEditDto) {
        return userAccountService.updateUser(login, userEditDto);
    }


    @PutMapping("/user/{login}/role/{role}")
    public RolesDto addRole(@PathVariable String login,@PathVariable String role) {
        return userAccountService.changeRolesList(login, role, false);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
    userAccountService.changePassword(principal.getName(), newPassword);
    }
}
