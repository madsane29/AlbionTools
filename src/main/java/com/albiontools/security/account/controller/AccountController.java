package com.albiontools.security.account.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

	private static final String ROOT_OF_CLASS = "/user";
	private static final String PATH_LOGIN = "/login";
	private static final String PATH_LOGOUT_SUCCESS = "/logout-success";
	private static final String PATH_DISABLED_ACCOUNT = "/disabled-account";
	private static final String PATH_BAD_CREDENTIALS = "/bad-credentials";
	private static final String PATH_REGISTRATION = "/registration";
	private static final String PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT = "/send-email-to-verificate-account";
	private static final String PATH_SEND_EMAIL_TO_CHANGE_PASSWORD = "/send-email-to-change-password";
	private static final String PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN = "/forgot-password-send-email-with-token";
	private static final String PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN = "/account-verification-send-email-with-token";
	private static final String PATH_CONFIRM_ACCOUNT = "/confirm-account";
	private static final String PATH_VALID_CODE = "/valid-code";
	private static final String PATH_INVALID_CODE = "/invalid-code";
	private static final String PATH_CONFIRM_RESET = "/confirm-reset";
	private static final String PATH_SET_NEW_PASSWORD = "/set-new-password";
	
	
	

	Logger logger = LoggerFactory.getLogger(getClass());
	private void loggerInfo(String path) {
		logger.info(ROOT_OF_CLASS + path + " is called");
	}
	
	private void loggerInfoAttributeAddedToModel(String path, String attribute) {
		//logger.info(ROOT_OF_CLASS + path + " is called");
	}
	
	private void loggerError(String path, List<ObjectError> errors) {
		logger.error(ROOT_OF_CLASS + path + " is called --> errors: ");
		for (ObjectError error : errors) {
			logger.error(error.toString());
		}
	}
	

	@Autowired
	private UserService userService;

	@GetMapping(PATH_LOGIN)
	public String getLoginPage() {
		loggerInfo(PATH_LOGIN);
		return "relatedToUserAccounts/login";
	}

	@GetMapping(PATH_LOGOUT_SUCCESS)
	public String getLogoutLoginPage(Model model) {
		loggerInfo(PATH_LOGOUT_SUCCESS);
		model.addAttribute("loggedOut", true);
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping(PATH_DISABLED_ACCOUNT)
	public String getDisabledAccountLoginPage(Model model) {
		loggerInfo(PATH_DISABLED_ACCOUNT);
		model.addAttribute("disabledAccount", true);

		return "relatedToUserAccounts/login";
	}
	
	@GetMapping(PATH_BAD_CREDENTIALS)
	public String getBadCredentialsLoginPage(Model model) {
		loggerInfo(PATH_BAD_CREDENTIALS);
		model.addAttribute("badCredentials", true);
		return "relatedToUserAccounts/login";
	}

	@GetMapping(PATH_REGISTRATION)
	public String getRegistrationPage(@ModelAttribute("user") User user) {
		loggerInfo(PATH_REGISTRATION);
		return "relatedToUserAccounts/registration";
	}

	@PostMapping(PATH_REGISTRATION)
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result,
			HttpServletResponse response) throws EmailAlreadyExistsException {
		loggerInfo(PATH_REGISTRATION);

		if (result.hasErrors()) {
			loggerError(PATH_REGISTRATION, result.getAllErrors());
			return "relatedToUserAccounts/registration";
		} else {
			userService.registerUser(user, response);
		}

		return "redirect:/user/login";
	}
	
	@GetMapping(PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT)
	public String getFormForEmailForVerificationCode(Model model) {
		loggerInfo(PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT);
		model.addAttribute("verificateAccountEmailForm", true);
		
		return "relatedToUserAccounts/form-to-get-email";
	}

	@GetMapping(PATH_SEND_EMAIL_TO_CHANGE_PASSWORD)
	public String getFormForEmailForNewPassword(Model model) {
		loggerInfo(PATH_SEND_EMAIL_TO_CHANGE_PASSWORD);
		model.addAttribute("forgotPasswordEmailForm", true);

		return "relatedToUserAccounts/form-to-get-email";
	}

	@RequestMapping(value = PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForPasswordChange(Model model,
			@RequestParam(name = "email", required = true) String email) {
		loggerInfo(PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN);
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
	
	@RequestMapping(value = PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForAccountVerification(Model model,
			@RequestParam(name = "email", required = true) String email) {
		loggerInfo(PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN);
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

	@GetMapping(PATH_CONFIRM_ACCOUNT)
	public String confirmUserAccount(@RequestParam(name = "token", required = true) String confirmationToken)
			throws NonExistentTokenException, MissingServletRequestParameterException {
		loggerInfo(PATH_CONFIRM_ACCOUNT);
		userService.confirmateAccount(confirmationToken);

		return "redirect:/user/valid-code";

	}

	@GetMapping(PATH_VALID_CODE)
	public String getVerificationSuccessPage(Model model) {
		loggerInfo(PATH_VALID_CODE);
		model.addAttribute("validCode", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}

	@GetMapping(PATH_INVALID_CODE)
	public String getVerificationFailedPage(Model model) {
		loggerInfo(PATH_INVALID_CODE);
		model.addAttribute("invalidCode", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}

	@GetMapping(PATH_CONFIRM_RESET)
	public String formForNewPasswordPage(RedirectAttributes redirectAttributes,
			@RequestParam(name = "token", required = true) String confirmationToken)
			throws MissingServletRequestParameterException, NonExistentTokenException {
		loggerInfo(PATH_CONFIRM_RESET);

		if (userService.getConfirmationToken(confirmationToken) != null) {
			redirectAttributes.addFlashAttribute("token", confirmationToken);
			return "redirect:/user/set-new-password";
		}

		return "redirect:/something-went-wrong";
	}

	@GetMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordForm() {
		loggerInfo(PATH_SET_NEW_PASSWORD);

		return "relatedToUserAccounts/change-password";
	}

	@PostMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordOfUserAccount(@RequestParam Map<String, String> parameters, Model model, RedirectAttributes redirectAttributes) {
		loggerInfo(PATH_SET_NEW_PASSWORD);
		
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
