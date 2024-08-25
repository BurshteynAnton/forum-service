package java53.forumservice.accounting.service;

import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.dto.RolesDto;
import java53.forumservice.accounting.dto.UserDto;
import java53.forumservice.accounting.dto.UserEditDto;
import java53.forumservice.accounting.dto.UserRegisterDto;
import java53.forumservice.accounting.dto.exception.UserExistsException;
import java53.forumservice.accounting.dto.exception.UserNotFoundException;
import java53.forumservice.accounting.model.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException();
        }
        User user = modelMapper.map(userRegisterDto, User.class);
        String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
        user.setPassword(password);
        userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }


    @Override
    public UserDto getUser(String login) {
        User user =  userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        User user = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);

        if (userEditDto.getFirstName() != null) {
            user.setFirstName(userEditDto.getFirstName());
        }
        if (userEditDto.getLastName() != null) {
            user.setLastName(userEditDto.getLastName());
        }
        userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        boolean res;
        if (isAddRole) {
            res = user.addRole(role);
        }else{
            res = user.removeRole(role);
        }
        if(res) {
            userAccountRepository.save(user);
        }
        return modelMapper.map(user, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
    User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
    String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    user.setPassword(password);
    userAccountRepository.save(user);
    }
}
