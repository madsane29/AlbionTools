package com.albiontools.security.account.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Proxy;

import com.albiontools.security.account.validator.PasswordMatches;
import com.albiontools.suggestions.model.Suggestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity(name = "users")
@PasswordMatches
public class User {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	//@Column(name="user_id")
	private Long id;

	@NotBlank(message = "{registration.usernameNotBlank}")
	@Size(min = 3, max = 32, message = "{registration.usernameSize}")
	private String username;

	@NotBlank(message = "{registration.passwordNotBlank}")
	@Size(min = 8, message = "{registration.passwordSize}")
	private String password;
	@Transient
	private String matchingPassword;

	//@ValidEmail
	@NotBlank(message = "{registration.emailNotBlank}")
	@Email(message = "{registration.emailValidFormat}")
	private String email;
	
	

	@OneToMany(mappedBy = "user")
	private List<Suggestion> suggestionsList = new ArrayList<>();
	
	


	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	//@JsonIgnore
	@JsonProperty(access = Access.READ_ONLY)
	private Set<Role> roles = new HashSet<>();

	private Boolean isAccountNonExpired = true;
	private Boolean isAccountNonLocked = true;
	private Boolean isCredentialsNonExpired = true;
	private Boolean isEnabled = false;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "token_id")
	private ConfirmationToken confirmationToken;
	
	

	public User() {
		super();
	}


	public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = new HashSet<>();
		}
		this.roles.add(new Role(roleName));
	}
	


	public Boolean getIsAccountNonExpired() {
		return isAccountNonExpired;
	}

	public void setIsAccountNonExpired(Boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public Boolean getIsAccountNonLocked() {
		return isAccountNonLocked;
	}

	public void setIsAccountNonLocked(Boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public Boolean getIsCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	public void setIsCredentialsNonExpired(Boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}
*/
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public ConfirmationToken getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(ConfirmationToken confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

}
