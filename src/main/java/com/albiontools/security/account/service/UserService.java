package com.albiontools.security.account.service;

import javax.servlet.http.HttpServletResponse;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.ConfirmationToken;
import com.albiontools.security.account.model.User;

public interface UserService {
	void registerUser(User user, HttpServletResponse response) throws EmailAlreadyExistsException;
	void changePassword(String email, String password, String matchesPassword, HttpServletResponse response) throws PasswordsNotMatchException;
	void newTokenForVerification(String email) throws NonExistentEmailException;
	void newTokenForForgotPassword(String email) throws NonExistentEmailException;
    
    void confirmateAccount(String confirmationToken) throws NonExistentTokenException;
	//Boolean emailExists(String email);
	
	ConfirmationToken getConfirmationToken(String confirmationToken) throws NonExistentTokenException;

		

	
}