package java53.forumservice.accounting.service;

import java53.forumservice.accounting.dao.UserAccountRepository;
import java53.forumservice.accounting.dto.RolesDto;
import java53.forumservice.accounting.dto.UserDto;
import java53.forumservice.accounting.dto.UserEditDto;
import java53.forumservice.accounting.dto.UserRegisterDto;
import java53.forumservice.accounting.dto.exception.UserNotFoundException;
import java53.forumservice.accounting.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, ModelMapper modelMapper) {
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        //TODO
        return null;
    }

    @Override
    public UserDto getUser(String login) {
        return null;
    }

    @Override
    public UserDto removeUser(String login) {
        User user = userAccountRepository.findByLogin(login)
                .orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        User user = userAccountRepository.findByLogin(login)
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
        User user = userAccountRepository.findByLogin(login)
                .orElseThrow(UserNotFoundException::new);
        if (isAddRole) {
            user.getRoles().add(role);
        } else {
            user.getRoles().remove(role);
        }

        userAccountRepository.save(user);
        RolesDto rolesDto = new RolesDto();
        rolesDto.setRoles(user.getRoles());
        return rolesDto;
    }

    @Override
    public void changePassword(String login, String newPassword) {
    //TODO
    }
}
