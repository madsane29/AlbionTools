package com.albiontools.security.account.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {


		Logger logger = LoggerFactory.getLogger(getClass());
		logger.warn("Exception: " + exception.getMessage());
		if (exception instanceof BadCredentialsException) response.sendRedirect("/user/bad-credentials");
		else if (exception instanceof DisabledException) response.sendRedirect("/user/disabled-account");

	}

}
