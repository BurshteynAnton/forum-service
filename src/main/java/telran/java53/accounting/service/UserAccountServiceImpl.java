package telran.java53.accounting.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java53.accounting.dao.UserAccountRepository;
import telran.java53.accounting.dto.RolesDto;
import telran.java53.accounting.dto.UserDto;
import telran.java53.accounting.dto.UserEditDto;
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
@Slf4j
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner, UserDetailsService {

	private final static String USER_NOT_FOUND_MSG = "User with %s Not Found";
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
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
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role);
		} else {
			res = userAccount.removeRole(role);
		}
		if (res) {
			userAccountRepository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
		String password = bCryptPasswordEncoder.encode(newPassword);
		userAccount.setPassword(password);
		userAccountRepository.save(userAccount);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String password = bCryptPasswordEncoder.encode("admin");

			Set<Role> roles = new HashSet<>();
			roles.add(Role.MODERATOR);
			roles.add(Role.ADMINISTRATOR);
			roles.add(Role.USER);

			UserAccount userAccount = new UserAccount("admin", "",
					"", "admin@example.com", password, roles);

			userAccountRepository.save(userAccount);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userAccountRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}

	public String signUpUser(UserAccount userAccount) {
		// Проверка существования пользователя
		if (userAccountRepository.findByEmail(userAccount.getEmail()).isPresent()) {
			throw new IllegalStateException("Email already taken");
		}

		// Кодирование пароля
		String encodedPassword = bCryptPasswordEncoder.encode(userAccount.getPassword());
		userAccount.setPassword(encodedPassword);

		// Устанавливаем статус пользователя как неактивный
		userAccount.setEnabled(false);

		// Сохранение пользователя
		userAccountRepository.save(userAccount);

		// Генерация токена подтверждения
		String token = UUID.randomUUID().toString();

		// Создание объекта ConfirmationToken
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				userAccount
		);

		// Сохранение токена
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		// Логирование
		log.info("User registered: {}. Confirmation token created.", userAccount.getEmail());

		return token;
	}

//	public String signUpUser(UserAccount userAccount) {
//		boolean userExists = userAccountRepository
//				.findByEmail(userAccount.getEmail())
//				.isPresent();
//		if (userExists) {
//			throw new IllegalStateException("email already taken");
//		}
//		String encodedPassword = bCryptPasswordEncoder.encode(userAccount.getPassword());
//		userAccount.setPassword(encodedPassword);
//
//		userAccountRepository.save(userAccount);
//
//		String token = UUID.randomUUID().toString();
//
//		ConfirmationToken confirmationToken = new ConfirmationToken(
//				token,
//				LocalDateTime.now(),
//				LocalDateTime.now().plusMinutes(15),
//				userAccount
//		);
//		confirmationTokenService.saveConfirmationToken(confirmationToken);
//
//		return token;
//	}

	public UserDto updateUserAccountStatus(String login, boolean enabled) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);
		userAccount.setEnabled(enabled);
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}
}
