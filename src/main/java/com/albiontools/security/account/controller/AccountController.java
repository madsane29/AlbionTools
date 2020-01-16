package com.albiontools.security.account.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.EmptyTokenFieldException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.service.UserService;

@Controller
@RequestMapping("/user")
public class AccountController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String getLoginPage() {
		logger.info("Login page");
		return "relatedToUserAccounts/login";
	}

	@GetMapping("/logout-success")
	public String getLogoutLoginPage(Model model) {
		model.addAttribute("loggedOut", true);

		logger.info("Login page");
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping("/disabled-account")
	public String getDisabledAccountLoginPage(Model model) {
		model.addAttribute("disabledAccount", true);
		
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping("/bad-credentials")
	public String getBadCredentialsLoginPage(Model model) {
		model.addAttribute("badCredentials", true);
		return "relatedToUserAccounts/login";
	}

	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") User user) {

		return "relatedToUserAccounts/registration";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result,
			HttpServletResponse response) throws EmailAlreadyExistsException {

		if (result.hasErrors()) {
			return "relatedToUserAccounts/registration";
		} else {
			userService.registerUser(user, response);
		}

		return "redirect:/user/login";
	}
	
	@GetMapping("/send-email-to-verificate-account")
	public String getFormForEmailForVerificationCode(Model model) {
		model.addAttribute("verificateAccountEmailForm", true);
		
		return "relatedToUserAccounts/form-to-get-email";
	}

	@GetMapping("/send-email-to-change-password")
	public String getFormForEmailForNewPassword(Model model) {
		model.addAttribute("forgotPasswordEmailForm", true);

		return "relatedToUserAccounts/form-to-get-email";
	}

	@RequestMapping(value = "/forgot-password-send-email-with-token", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForPasswordChange(Model model,
			@RequestParam(name = "email", required = true) String email) {
		if (email != null) {
			model.addAttribute("email", email);
			try {
				userService.sendEmailWithTokenToChangeAccountPassword(email);
				return "relatedToUserAccounts/email-sent-with-token";
			} catch (NonExistentEmailException e) {
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return "relatedToUserAccounts/form-to-get-email";
	}
	
	@RequestMapping(value = "/account-verification-send-email-with-token", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForAccountVerification(Model model,
			@RequestParam(name = "email", required = true) String email) {
		if (email != null) {
			model.addAttribute("email", email);
			try {
				userService.sendEmailWithTokenToVerificateAccount(email);
				return "relatedToUserAccounts/email-sent-with-token";
			} catch (NonExistentEmailException e) {
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return "relatedToUserAccounts/form-to-get-email";
	}

	@GetMapping(value = "/confirm-account")
	public String confirmUserAccount(@RequestParam(name = "token", required = true) String confirmationToken)
			throws NonExistentTokenException, MissingServletRequestParameterException {
		userService.confirmateAccount(confirmationToken);

		return "redirect:/user/valid-code";

	}

	@GetMapping("/valid-code")
	public String getVerificationSuccessPage(Model model) {
		model.addAttribute("validCode", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}

	@GetMapping("/invalid-code")
	public String getVerificationFailedPage(Model model) {
		model.addAttribute("invalidCode", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}

	@GetMapping(value = "/confirm-reset")
	public String formForNewPasswordPage(RedirectAttributes redirectAttributes,
			@RequestParam(name = "token", required = true) String confirmationToken)
			throws MissingServletRequestParameterException, NonExistentTokenException {

		if (userService.getConfirmationToken(confirmationToken) != null) {
			redirectAttributes.addFlashAttribute("token", confirmationToken);
			return "redirect:/user/set-new-password";
		}

		return "redirect:/something-went-wrong";
	}

	@GetMapping(value = "/set-new-password")
	public String changePasswordForm() {

		return "relatedToUserAccounts/change-password";
	}

	@PostMapping(value = "/set-new-password")
	public String changePasswordOfUserAccount(@RequestParam Map<String, String> parameters, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			userService.changePassword(parameters.get("token"), parameters.get("password"), parameters.get("matchesPassword"));
		} catch (PasswordsNotMatchException e) {
			model.addAttribute("passwordsDoNotMatch", true);
			return "relatedToUserAccounts/change-password";
		} catch (EmptyTokenFieldException e) {
			return "redirect:/user/invalid-code";
		}

		redirectAttributes.addFlashAttribute("userAccountPasswordChanged", true);
		return "redirect:/user/login";

	}

}
