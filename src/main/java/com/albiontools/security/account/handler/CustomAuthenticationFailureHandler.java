package com.albiontools.security.account.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.albiontools.logger.CustomLogger;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		if (exception instanceof DisabledException) {
			response.sendRedirect("/user/disabled-account");
		} else
		if (exception instanceof BadCredentialsException) { 
			response.sendRedirect("/user/bad-credentials");
		}
		

	}

}
