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

import com.albiontools.security.account.exception.EmailExistsException;
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
			HttpServletResponse response) throws EmailExistsException {

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
	public String sendEmailWithTokenForPasswordChange(Model model) {
		
		// Send email with new token so user can change the password
		
		

		return "redirect:/user/email-sent-with-code";
	}
	
	@GetMapping("/email-sent-with-code")
	public String getEmailSentWithTokenPage(Model model) {
		model.addAttribute("email", "email@email.email");

		return "relatedToUserAccounts/email-sent-with-token";
	}
	
	
	/**
	 * If the code is invalid --> validcode = true
	 * if the code is valid --> change password page or verificate account page
	 * @param model
	 * @return
	 */
	
	@GetMapping("/verification")
	public String getVerificationPage(Model model) {
		//model.addAttribute("validCode", true);
		//model.addAttribute("invalidCode", true);
		
		return "relatedToUserAccounts/verification";
	}
	
}
