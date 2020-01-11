package com.albiontools.security.account.controller;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.service.UserService;



@Controller
@RequestMapping("/user")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String getLoginPage() {
		
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping("/logout-success")
	public String getLogoutPage() {
		
		return "redirect:/";
	}
	
	
	
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") User user) {
		
		return "relatedToUserAccounts/registration";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount(
			@ModelAttribute("user") @Valid User user, 
			BindingResult result,
			HttpServletResponse response) throws EmailAlreadyExistsException {

		if (result.hasErrors()) {
			return "relatedToUserAccounts/registration";
		} else {
			userService.registerUser(user, response);
		}
		
		return "redirect:/user/login";
	}
	
	@GetMapping("/email-for-new-token")
	public String askForEmailToSendNewTokenToUserPage() {
		
		return "relatedToUserAccounts/form-to-get-email-for-new-password";
	}
	
	@PostMapping("/send-email-with-token")
	public String sendEmailWithTokenForPasswordChange(Model model, @RequestParam(name = "email", required = false) String email, HttpServletResponse response) {
		if (email != null) {
			try {
				userService.newTokenForForgotPassword(email);
				response.addHeader("email", email);
				return "redirect:/user/email-sent-with-code";
			} catch (NonExistentEmailException e) {
				model.addAttribute("email", email);
				model.addAttribute("nonExistentEmail", true);
				return "relatedToUserAccounts/form-to-get-email-for-new-password";
			}
			
		}
		
		
		return "relatedToUserAccounts/form-to-get-email-for-new-password";
	}
	
	@GetMapping("/email-sent-with-code")
	public String getEmailSentWithTokenPage(Model model, HttpServletResponse response) {
		model.addAttribute("email", response.getHeader("email"));

		return "relatedToUserAccounts/email-sent-with-token";
	}
	
	
	@RequestMapping(value = "/confirm-account", method = RequestMethod.GET)
	public String confirmUserAccount(@RequestParam(name = "token", required = false) String confirmationToken)
			throws NonExistentTokenException {
		userService.confirmateAccount(confirmationToken);
		
		return "redirect:/user/verification-success";

	}
	
	@GetMapping("/verification-success")
	public String getVerificationSuccessPage(Model model) {
		model.addAttribute("validCode", true);
		
		return "relatedToUserAccounts/verification";
	}
	
	@GetMapping("/verification-failed")
	public String getVerificationFailedPage(Model model) {
		model.addAttribute("invalidCode", true);
		
		return "relatedToUserAccounts/verification";
	}
	
}
