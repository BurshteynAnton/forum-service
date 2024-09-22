package telran.java53.accounting.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class UserAccount implements UserDetails {

	@Id
	private String login;

	private String firstName;
	private String lastName;
	private String email;
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_login"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>();

	private boolean enabled = false;
	private boolean locked = false;


//	public UserAccount() {
//		roles.add(Role.USER);
//	}

	public UserAccount(String login, String firstName, String lastName, String email, String password, Set<Role> roles) {
		this();
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
	}

	public boolean addRole(String role) {
		return roles.add(Role.valueOf(role.toUpperCase()));
	}

	public boolean removeRole(String role) {
		return roles.remove(Role.valueOf(role.toUpperCase()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
