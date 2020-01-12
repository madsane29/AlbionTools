package com.albiontools.security.account.handler;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
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
/*
	@ExceptionHandler(PasswordsNotMatchException.class)
	public ModelAndView passwordsNotMatchExceptionHandler(HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("changePassword");
		mav.addObject("email", response.getHeader("email"));
		mav.addObject("passwordsNotMatch", true);

		return mav;
	}
	*/
	@ExceptionHandler(NonExistentTokenException.class)
	public String nonExistentTokenExceptionHandler() {

		return "redirect:/user/verification-failed";
	}
}
