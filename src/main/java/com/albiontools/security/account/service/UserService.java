package com.albiontools.security.account.service;

import javax.servlet.http.HttpServletResponse;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.EmptyTokenFieldException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordIsBlankException;
import com.albiontools.security.account.exception.PasswordIsTooShortException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.ConfirmationToken;
import com.albiontools.security.account.model.User;

public interface UserService {
	void registerUser(User user, HttpServletResponse response) throws EmailAlreadyExistsException;
	void sendEmailWithTokenToVerificateAccount(String email) throws NonExistentEmailException;
	void sendEmailWithTokenToChangeAccountPassword(String email) throws NonExistentEmailException;
    
    void confirmateAccount(String confirmationToken) throws NonExistentTokenException;
	
	ConfirmationToken getConfirmationToken(String confirmationToken) throws NonExistentTokenException;
	void changePassword(String token, String password, String matchesPassword) throws PasswordsNotMatchException, EmptyTokenFieldException, PasswordIsTooShortException, PasswordIsBlankException;
	User setNewTokenForUser(String email) throws NonExistentEmailException;

	User getUserByConfirmationToken(String confirmationToken);
	User getUserByConfirmationToken(ConfirmationToken confirmationToken);

		

	
}