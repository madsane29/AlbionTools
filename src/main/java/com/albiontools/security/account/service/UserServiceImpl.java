package com.albiontools.security.account.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
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

	@Override
	public void confirmateAccount(String confirmationToken) throws NonExistentTokenException {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

		if (token != null) {
			User user = userRepository.findByEmail(token.getUser().getEmail());
			user.setMatchingPassword(user.getPassword());
			user.setIsEnabled(true);
			user.setConfirmationToken(null);
			userRepository.save(user);
		} else
			throw new NonExistentTokenException("The token does not exists!");

	}
	
	@Override
	public void newTokenForVerification(String email) throws NonExistentEmailException {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setMatchingPassword(user.getPassword());
			user.setConfirmationToken(confirmationTokenRepository.save(new ConfirmationToken(user)));

			userRepository.save(user);
			
			emailSenderService.sendVerificationEmail(user);

		}
	}

	@Override
	public void newTokenForForgotPassword(String email) throws NonExistentEmailException{
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setMatchingPassword(user.getPassword());
			user.setConfirmationToken(confirmationTokenRepository.save(new ConfirmationToken(user)));
			userRepository.save(user);

			emailSenderService.sendForgotPasswordEmail(user);
		} else {
			throw new NonExistentEmailException("The following email address does not exists: " + email);
		}
	}

	@Override
	public void changePassword(String email, String password, String matchesPassword)
			throws PasswordsNotMatchException {
		User user = userRepository.findByEmail(email);

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
	
	

}