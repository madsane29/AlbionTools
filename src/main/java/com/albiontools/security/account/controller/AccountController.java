package com.albiontools.security.account.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.servlet.view.RedirectView;

import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.EmptyTokenFieldException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.ConfirmationToken;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.repository.UserRepository;
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
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result,
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

	@RequestMapping(value = "/send-email-with-token", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForPasswordChange(Model model,
			@RequestParam(name = "email", required = true) String email) {
		if (email != null) {

			model.addAttribute("email", email);
			try {
				userService.newTokenForForgotPassword(email);
				return "relatedToUserAccounts/email-sent-with-token";
			} catch (NonExistentEmailException e) {
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return "relatedToUserAccounts/form-to-get-email-for-new-password";
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
	public String changePasswordOfUserAccount(@RequestParam Map<String, String> body, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			userService.changePassword(body.get("token"), body.get("password"), body.get("matchesPassword"));
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
