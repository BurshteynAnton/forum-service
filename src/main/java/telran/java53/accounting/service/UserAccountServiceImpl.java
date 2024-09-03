package telran.java53.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java53.accounting.dao.UserAccountRepository;
import telran.java53.accounting.dto.RolesDto;
import telran.java53.accounting.dto.UserDto;
import telran.java53.accounting.dto.UserEditDto;
import telran.java53.accounting.dto.UserRegisterDto;
import telran.java53.accounting.dto.exceptions.UserExistsException;
import telran.java53.accounting.dto.exceptions.UserNotFoundException;
import telran.java53.accounting.model.Role;
import telran.java53.accounting.model.UserAccount;
import telran.java53.registration.token.ConfirmationToken;
import telran.java53.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner, UserDetailsService {

	private final static String USER_NOT_FOUND_MSG = "User with %s Not Found";
	final UserAccountRepository userAccountRepository;
	@Lazy
	final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;



	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		String password = bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role);
		} else {
			res = userAccount.removeRole(role);
		}
		if(res) {
			userAccountRepository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		String password = bCryptPasswordEncoder.encode(newPassword);
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);

	}

	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String password = bCryptPasswordEncoder.encode("admin");

			// Создаем и инициализируем набор ролей
			Set<Role> roles = new HashSet<>();
			roles.add(Role.MODERATOR);
			roles.add(Role.ADMINISTRATOR);
			UserAccount userAccount = new UserAccount("admin", "",
					"", "anton@example.com", password, roles);

			// Сохраняем объект в репозитории
			userAccountRepository.save(userAccount);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userAccountRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}

	public String signUpUser(UserAccount userAccount) {
		boolean userExists = userAccountRepository
				.findByEmail(userAccount.getEmail())
				.isPresent();
		if(userExists){

			//TODO if email not confirmed send comfirmation mail
			//TODO check of attributes are the same and
			//else exception
			throw new IllegalStateException(("email already taken"));
		}
		String encodedPassword = bCryptPasswordEncoder
				.encode(userAccount.getPassword());

		userAccount.setPassword(encodedPassword);

		userAccountRepository.save(userAccount);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				userAccount
		);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		//TODO: send email

		return token;
	}
	public UserDto updateUserAccountStatus(String login, boolean enabled) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userAccount.setEnabled(enabled);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

//	public int enableUserAccount(String email) {
//		return userAccountRepository.enableUserAccount(email);
//	}

}
