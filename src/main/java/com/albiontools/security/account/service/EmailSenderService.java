package com.albiontools.security.account.service;

import java.util.Locale;

import com.albiontools.security.account.model.User;

public interface EmailSenderService {
	//void sendVerificationEmail(User user, Locale locale);
	//void sendForgotPasswordEmail(User user, Locale locale);
	void sendVerificationEmail(User user);
	void sendForgotPasswordEmail(User user);
}
