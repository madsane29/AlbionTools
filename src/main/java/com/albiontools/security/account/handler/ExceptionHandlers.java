package com.albiontools.security.account.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.NonExistentEmailException;

@ControllerAdvice
public class ExceptionHandlers {
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public String emailAlreadyExistsExceptionHandler(Model model, HttpServletResponse response) {
		User user = new User();
		user.setUsername(response.getHeader("username"));
		user.setEmail(response.getHeader("email"));
		model.addAttribute("user", user);
		
		model.addAttribute("emailAlreadyExists", true);
		
		return "relatedToUserAccounts/registration";
	}

	@ExceptionHandler(NonExistentTokenException.class)
	public String nonExistentTokenExceptionHandler(Model model, HttpServletRequest request) {
		//System.out.println(request.getRequestURI());
		
		String requestURI = request.getRequestURI();
		if (requestURI.equals("/user/confirm-reset")) return "redirect:/user/invalid-change-password-code";
		else if (requestURI.equals("/user/confirm-account")) return "redirect:/user/invalid-verification-code";
		
		
		return "redirect:/user/login";
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class) 
	public String missingServletRequestParameterExceptionHandler(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		if (requestURI.equals("/user/confirm-reset")) return "redirect:/user/invalid-change-password-code";
		else if (requestURI.equals("/user/confirm-account")) return "redirect:/user/invalid-verification-code";
		
		return "redirect:/user/login";
	}
}
