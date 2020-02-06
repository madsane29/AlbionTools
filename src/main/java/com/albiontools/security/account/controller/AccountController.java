package com.albiontools.security.account.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.albiontools.logger.CustomLogger;
import com.albiontools.security.account.exception.EmailAlreadyExistsException;
import com.albiontools.security.account.exception.EmptyTokenFieldException;
import com.albiontools.security.account.exception.NonExistentEmailException;
import com.albiontools.security.account.exception.NonExistentTokenException;
import com.albiontools.security.account.exception.PasswordIsBlankException;
import com.albiontools.security.account.exception.PasswordIsTooShortException;
import com.albiontools.security.account.exception.PasswordsNotMatchException;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.repository.UserRepository;
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
	private static final String PATH_INVALID_CHANGE_PASSWORD_CODE = "/invalid-change-password-code";
	private static final String PATH_INVALID_VERIFICATION_CODE = "/invalid-verification-code";
	private static final String PATH_CONFIRM_RESET = "/confirm-reset";
	private static final String PATH_SET_NEW_PASSWORD = "/set-new-password";
	
	
	private CustomLogger customLogger = new CustomLogger(getClass());

	@Autowired
	private UserService userService;

	@GetMapping({"", "/**"})
	public String loginPage() {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
		return "redirect:";
	}
	
	@GetMapping(PATH_LOGIN)
	public String getLoginPage(HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_LOGIN, request);
		return "relatedToUserAccounts/login";
	}

	@GetMapping(PATH_LOGOUT_SUCCESS)
	public String getLogoutLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_LOGOUT_SUCCESS, request);
		model.addAttribute("loggedOut", true);
		return "relatedToUserAccounts/login";
	}
	
	@GetMapping(PATH_DISABLED_ACCOUNT)
	public String getDisabledAccountLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_DISABLED_ACCOUNT, request);
		model.addAttribute("disabledAccount", true);

		return "relatedToUserAccounts/login";
	}
	
	@GetMapping(PATH_BAD_CREDENTIALS)
	public String getBadCredentialsLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_BAD_CREDENTIALS, request);
		model.addAttribute("badCredentials", true);
		return "relatedToUserAccounts/login";
	}

	@GetMapping(PATH_REGISTRATION)
	public String getRegistrationPage(@ModelAttribute("user") User user, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_REGISTRATION, request);
		return "relatedToUserAccounts/registration";
	}

	@PostMapping(PATH_REGISTRATION)
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result,
			HttpServletResponse response, HttpServletRequest request) throws EmailAlreadyExistsException {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_REGISTRATION, request);

		if (result.hasErrors()) {
			customLogger.loggerError(ROOT_OF_CLASS+PATH_REGISTRATION, result.getAllErrors(), request);
			return "relatedToUserAccounts/registration";
		} else {
			customLogger.loggerInfoWithMessage(ROOT_OF_CLASS+PATH_REGISTRATION, request, "User created: " + user.toString());
			userService.registerUser(user, response);
		}

		return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
	}
	
	@GetMapping(PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT)
	public String getFormForEmailForVerificationCode(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT, request);
		model.addAttribute("verificateAccountEmailForm", true);
		
		return "relatedToUserAccounts/form-to-get-email";
	}

	@GetMapping(PATH_SEND_EMAIL_TO_CHANGE_PASSWORD)
	public String getFormForEmailForNewPassword(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_SEND_EMAIL_TO_CHANGE_PASSWORD, request);
		model.addAttribute("forgotPasswordEmailForm", true);

		return "relatedToUserAccounts/form-to-get-email";
	}

	@RequestMapping(value = PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, method = {  RequestMethod.POST })
	public String sendEmailWithTokenForPasswordChange(Model model,
			@RequestParam(name = "email", required = true) String email, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, request);
		if (email != null) {
			model.addAttribute("email", email);
			try {
				customLogger.loggerInfoWithMessage(ROOT_OF_CLASS+PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, request, "Email sent with token to change password: " + email);
				userService.sendEmailWithTokenToChangeAccountPassword(email);
				return "relatedToUserAccounts/email-sent-with-token";
			} catch (NonExistentEmailException e) {
				customLogger.loggerWarn(ROOT_OF_CLASS+PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, request, e);
				model.addAttribute("forgotPasswordEmailForm", true);
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return "relatedToUserAccounts/form-to-get-email";
	}
	
	@RequestMapping(value = PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForAccountVerification(Model model,
			@RequestParam(name = "email", required = true) String email, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, request);
		if (email != null) {
			model.addAttribute("email", email);
			try {
				customLogger.loggerInfoWithMessage(ROOT_OF_CLASS+PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, request, "Email sent with token to verificate account: " + email);
				userService.sendEmailWithTokenToVerificateAccount(email);
				return "relatedToUserAccounts/email-sent-with-token";
			} catch (NonExistentEmailException e) {
				customLogger.loggerWarn(ROOT_OF_CLASS+PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, request, e);
				model.addAttribute("verificateAccountEmailForm", true);
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return "relatedToUserAccounts/form-to-get-email";
	}

	@GetMapping(PATH_CONFIRM_ACCOUNT)
	public String confirmUserAccount(@RequestParam(name = "token", required = true) String confirmationToken, HttpServletRequest request)
			throws NonExistentTokenException, MissingServletRequestParameterException {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_CONFIRM_ACCOUNT, request);
		userService.confirmateAccount(confirmationToken);

		return "redirect:" + ROOT_OF_CLASS + PATH_VALID_CODE;

	}

	@GetMapping(PATH_VALID_CODE)
	public String getVerificationSuccessPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_VALID_CODE, request);
		model.addAttribute("validCode", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}
	@GetMapping(PATH_INVALID_VERIFICATION_CODE)
	public String getVerificationFailedPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_INVALID_VERIFICATION_CODE, request);
		model.addAttribute("invalidCodeVerificateAccount", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}
	
	@GetMapping(PATH_INVALID_CHANGE_PASSWORD_CODE)
	public String getChangePasswordFailedPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_INVALID_CHANGE_PASSWORD_CODE, request);
		model.addAttribute("invalidCodeChangePassword", true);

		return "relatedToUserAccounts/valid-invalid-code";
	}
	
	@GetMapping(PATH_CONFIRM_RESET)
	public String formForNewPasswordPage(RedirectAttributes redirectAttributes,
			@RequestParam(name = "token", required = true) String confirmationToken, HttpServletRequest request)
			throws MissingServletRequestParameterException, NonExistentTokenException {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_CONFIRM_RESET, request);
		
		if (userService.getConfirmationToken(confirmationToken) != null) {
			customLogger.loggerInfoWithMessage(ROOT_OF_CLASS+PATH_CONFIRM_RESET, request, "Confirmation token exists: " + confirmationToken);
			redirectAttributes.addFlashAttribute("token", confirmationToken);
			return "redirect:" + ROOT_OF_CLASS + PATH_SET_NEW_PASSWORD;
		}

		return null;
	}
	


	@GetMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordForm(HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request);
		return "relatedToUserAccounts/change-password";
	}

	@PostMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordOfUserAccount(@RequestParam Map<String, String> parameters, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		customLogger.loggerInfoIsCalled(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request);
		
		try {
			userService.changePassword(parameters.get("token"), parameters.get("password"), parameters.get("matchesPassword"));
		} catch (PasswordsNotMatchException e) {
			customLogger.loggerWarn(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request, e);
			model.addAttribute("passwordsDoNotMatch", true);
			model.addAttribute("token", parameters.get("token"));
			return "relatedToUserAccounts/change-password";
		} catch (EmptyTokenFieldException e) {
			customLogger.loggerWarn(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request, e);
			return "redirect:" + ROOT_OF_CLASS + PATH_INVALID_CHANGE_PASSWORD_CODE;
		} catch (PasswordIsTooShortException e) {
			customLogger.loggerWarn(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request, e);
			model.addAttribute("passwordIsTooShort", true);
			model.addAttribute("token", parameters.get("token"));
			return "relatedToUserAccounts/change-password";	
		} catch (PasswordIsBlankException e) {
			customLogger.loggerWarn(ROOT_OF_CLASS+PATH_SET_NEW_PASSWORD, request, e);
			model.addAttribute("passwordIsBlank", true);
			model.addAttribute("token", parameters.get("token"));
			return "relatedToUserAccounts/change-password";	
		}

		redirectAttributes.addFlashAttribute("userAccountPasswordChanged", true);
		return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
	}
	
	
}
