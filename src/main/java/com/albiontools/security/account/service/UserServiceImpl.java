package com.albiontools.security.account.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.EmptyTokenFieldException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordIsBlankException;
import com.albiontools.security.account.exception.PasswordIsTooShortException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.ConfirmationToken;
import com.albiontools.security.account.model.Role;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.repository.ConfirmationTokenRepository;
import com.albiontools.security.account.repository.RoleRepository;
import com.albiontools.security.account.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final String ROLE_USER = "ROLE_USER";

	@Transactional
	@Override
	public void registerUser(User user, HttpServletResponse response) throws EmailAlreadyExistsException {
		if (userRepository.findByEmail(user.getEmail()) != null) {
			response.addHeader("username", user.getUsername());
			response.addHeader("email", user.getEmail());
			throw new EmailAlreadyExistsException("Email exists: " + user.getEmail());
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setMatchingPassword(user.getPassword());
		user.setConfirmationToken(confirmationTokenRepository.save(new ConfirmationToken(user)));

		Role userRole = roleRepository.findByName(ROLE_USER);

		if (userRole != null) {
			user.getRoles().add(userRole);
		} else {
			user.addRoles(ROLE_USER);
		}

		userRepository.save(user);
		emailSenderService.sendVerificationEmail(user);
	}

	@Transactional
	@Override
	public void confirmateAccount(String confirmationToken) throws NonExistentTokenException {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

		if (token != null) {
			//User user = userRepository.findByEmail(token.getUser().getEmail());
			User user = getUserByConfirmationToken(token);
			System.out.println(user.toString());
			user.setMatchingPassword(user.getPassword());
			user.setIsEnabled(true);
			user.setConfirmationToken(null);
			userRepository.save(user);
		} else
			throw new NonExistentTokenException("The token does not exists!");

	}
	
	@Transactional
	@Override
	public User setNewTokenForUser(String email) throws NonExistentEmailException {
		User user = userRepository.findByEmail(email);
			
		if (user == null) throw new NonExistentEmailException("The following email address does not exists: " + email);
		user.setMatchingPassword(user.getPassword());
		user.setConfirmationToken(confirmationTokenRepository.save(new ConfirmationToken(user)));
		
		return userRepository.save(user);	
	}

	@Transactional
	@Override
	public void sendEmailWithTokenToVerificateAccount(String email) throws NonExistentEmailException {
		User user = setNewTokenForUser(email);
		emailSenderService.sendVerificationEmail(user);
	}

	@Transactional
	@Override
	public void sendEmailWithTokenToChangeAccountPassword(String email) throws NonExistentEmailException {
		User user = setNewTokenForUser(email);
		emailSenderService.sendForgotPasswordEmail(user);
	}

	@Transactional
	@Override
	public void changePassword(String confirmationToken, String password, String matchesPassword)
			throws PasswordsNotMatchException, EmptyTokenFieldException, PasswordIsTooShortException, PasswordIsBlankException {
		User user;
		if (!confirmationToken.equals("")) {
			user = getUserByConfirmationToken(confirmationToken);
		} else {
			throw new EmptyTokenFieldException("Token is blank!");
		}
		if (password.replaceAll(" ", "").length() == 0) throw new PasswordIsBlankException("Password can't be blank");
		if (password.length() < 8) throw new PasswordIsTooShortException("Password must be 8 characters or more!");
		
		if (password.equals(matchesPassword)) {
			user.setPassword(passwordEncoder.encode(password));
			user.setMatchingPassword(user.getPassword());
			user.setIsEnabled(true);
			user.setConfirmationToken(null);

			userRepository.save(user);
		} else {
			throw new PasswordsNotMatchException("Passwords do not match!");
		}
	}

	@Override
	public ConfirmationToken getConfirmationToken(String confirmationToken) throws NonExistentTokenException {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		if (token == null) {
			throw new NonExistentTokenException("The token does not exists!");
		}
		return token;
	}
	
	@Override
	public User getUserByConfirmationToken(String confirmationToken) {
		return confirmationTokenRepository.findByConfirmationToken(confirmationToken).getUser();
	}

	@Override
	public User getUserByConfirmationToken(ConfirmationToken confirmationToken) {
		return confirmationTokenRepository.findByConfirmationToken(confirmationToken.getConfirmationToken()).getUser();
	}
}