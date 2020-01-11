package com.albiontools.security.account.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class AccountController {
	
	@GetMapping("/login")
	public String getLoginPage() {
		
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping("/logout-success")
	public String getLogoutPage() {
		
		return "redirect:/";
	}
	
	
	
	@GetMapping("/registration")
	public String getRegistrationPage() {
		
		return "relatedToUserAccounts/registration";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount() {

		//return null;
		return "redirect:/user/login";
	}
	
	@GetMapping("/forgot-password")
	public String askForEmailToSendNewTokenToUserPage() {
		
		return "relatedToUserAccounts/form-to-get-email-for-new-password";
	}
	
	@PostMapping("/send-email-with-token")
	public String sendEmailWithTokenForPasswordChange(Model model) {
		
		// Send email with new token so user can change the password
		

		return "redirect:/user/email-sent-with-code";
	}
	
	@GetMapping("/email-sent-with-code")
	public String getEmailSentWithTokenPage(Model model) {
		model.addAttribute("email", "email@email.email");

		return "relatedToUserAccounts/email-sent-with-token";
	}
	
}
