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
	
	private static final String accountRelatedHTMLFilesFolder = "relatedToUserAccounts/";
	
	
	private CustomLogger customLogger = new CustomLogger(getClass());

	@Autowired
	private UserService userService;

	@GetMapping({"", "/**"})
	public String loginPage() {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
		return "redirect:/";
	}
	
	@GetMapping(PATH_LOGIN)
	public String getLoginPage(HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) return "redirect:/";
		return accountRelatedHTMLFilesFolder + "login";
	}

	@GetMapping(PATH_LOGOUT_SUCCESS)
	public String getLogoutLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("loggedOut", true);
		return accountRelatedHTMLFilesFolder + "login";
	}
	
	@GetMapping(PATH_DISABLED_ACCOUNT)
	public String getDisabledAccountLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("disabledAccount", true);

		return accountRelatedHTMLFilesFolder + "login";
	}
	
	@GetMapping(PATH_BAD_CREDENTIALS)
	public String getBadCredentialsLoginPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("badCredentials", true);
		return accountRelatedHTMLFilesFolder + "login";
	}

	@GetMapping(PATH_REGISTRATION)
	public String getRegistrationPage(@ModelAttribute("user") User user, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		return accountRelatedHTMLFilesFolder + "registration";
	}

	@PostMapping(PATH_REGISTRATION)
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result,
			HttpServletResponse response, HttpServletRequest request) throws EmailAlreadyExistsException {
		customLogger.loggerInfoWithHttpServletRequestParam(request);

		if (result.hasErrors()) {
			customLogger.loggerErrorWithHttpServletRequestAndErrorsParam(request, result.getAllErrors());
			return accountRelatedHTMLFilesFolder + "registration";
		} else {
			customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Trying to create: " + user.getEmail());
			userService.registerUser(user, response);
			customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Successfully created: " + user.getEmail());
		}

		return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
	}
	
	@GetMapping(PATH_SEND_EMAIL_TO_VERIFICATE_ACCOUNT)
	public String getFormForEmailForVerificationCode(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("verificateAccountEmailForm", true);
		
		return accountRelatedHTMLFilesFolder + "form-to-get-email";
	}

	@GetMapping(PATH_SEND_EMAIL_TO_CHANGE_PASSWORD)
	public String getFormForEmailForNewPassword(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("forgotPasswordEmailForm", true);

		return accountRelatedHTMLFilesFolder + "form-to-get-email";
	}

	@RequestMapping(value = PATH_FORGOT_PASSWORD_SEND_EMAIL_WITH_TOKEN, method = {  RequestMethod.POST })
	public String sendEmailWithTokenForPasswordChange(Model model,
			@RequestParam(name = "email", required = true) String email, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		if (email != null) {
			model.addAttribute("email", email);
			try {
				customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Email sent with token to change password: " + email);
				userService.sendEmailWithTokenToChangeAccountPassword(email);
				return accountRelatedHTMLFilesFolder + "email-sent-with-token";
			} catch (NonExistentEmailException e) {
				customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
				model.addAttribute("forgotPasswordEmailForm", true);
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return accountRelatedHTMLFilesFolder + "form-to-get-email";
	}
	
	@RequestMapping(value = PATH_ACCOUNT_VERIFICATION_SEND_EMAIL_WITH_TOKEN, method = { RequestMethod.GET, RequestMethod.POST })
	public String sendEmailWithTokenForAccountVerification(Model model,
			@RequestParam(name = "email", required = true) String email, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		if (email != null) {
			model.addAttribute("email", email);
			try {
				customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Email sent with token to verificate account: " + email);
				userService.sendEmailWithTokenToVerificateAccount(email);
				return accountRelatedHTMLFilesFolder + "email-sent-with-token";
			} catch (NonExistentEmailException e) {
				customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
				model.addAttribute("verificateAccountEmailForm", true);
				model.addAttribute("nonExistentEmail", true);
			}
		}
		return accountRelatedHTMLFilesFolder + "form-to-get-email";
	}

	@GetMapping(PATH_CONFIRM_ACCOUNT)
	public String confirmUserAccount(@RequestParam(name = "token", required = true) String confirmationToken, HttpServletRequest request)
			throws NonExistentTokenException, MissingServletRequestParameterException {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		userService.confirmateAccount(confirmationToken);

		return "redirect:" + ROOT_OF_CLASS + PATH_VALID_CODE;

	}

	@GetMapping(PATH_VALID_CODE)
	public String getVerificationSuccessPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("validCode", true);

		return accountRelatedHTMLFilesFolder + "valid-invalid-code";
	}
	@GetMapping(PATH_INVALID_VERIFICATION_CODE)
	public String getVerificationFailedPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("invalidCodeVerificateAccount", true);

		return accountRelatedHTMLFilesFolder + "valid-invalid-code";
	}
	
	@GetMapping(PATH_INVALID_CHANGE_PASSWORD_CODE)
	public String getChangePasswordFailedPage(Model model, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		model.addAttribute("invalidCodeChangePassword", true);

		return accountRelatedHTMLFilesFolder + "valid-invalid-code";
	}
	
	@GetMapping(PATH_CONFIRM_RESET)
	public String formForNewPasswordPage(RedirectAttributes redirectAttributes,
			@RequestParam(name = "token", required = true) String confirmationToken, HttpServletRequest request)
			throws MissingServletRequestParameterException, NonExistentTokenException {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		
		if (userService.getConfirmationToken(confirmationToken) != null) {
			customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Confirmation token exists: " + confirmationToken);
			redirectAttributes.addFlashAttribute("token", confirmationToken);
			return "redirect:" + ROOT_OF_CLASS + PATH_SET_NEW_PASSWORD;
		}

		return null;
	}
	


	@GetMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordForm(HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);
		return accountRelatedHTMLFilesFolder + "change-password";
	}

	@PostMapping(PATH_SET_NEW_PASSWORD)
	public String changePasswordOfUserAccount(@RequestParam Map<String, String> parameters, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		customLogger.loggerInfoWithHttpServletRequestParam(request);

		try {
			customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Tries to change password for user with following token: " + parameters.get("token"));
			userService.changePassword(parameters.get("token"), parameters.get("password"),	parameters.get("matchesPassword"));
			customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Password has changed for user with the following token: " + parameters.get("token"));
		} catch (EmptyTokenFieldException e) {
			customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
			return "redirect:" + ROOT_OF_CLASS + PATH_INVALID_CHANGE_PASSWORD_CODE;
		} catch (PasswordsNotMatchException e) {
			customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
			model.addAttribute("passwordsDoNotMatch", true);
			model.addAttribute("token", parameters.get("token"));
			return accountRelatedHTMLFilesFolder + "change-password";
		} catch (PasswordIsTooShortException e) {
			customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
			model.addAttribute("passwordIsTooShort", true);
			model.addAttribute("token", parameters.get("token"));
			return accountRelatedHTMLFilesFolder + "change-password";
		} catch (PasswordIsBlankException e) {
			customLogger.loggerWarnWithHttpServletRequestAndExceptionParam(request, e);
			model.addAttribute("passwordIsBlank", true);
			model.addAttribute("token", parameters.get("token"));
			return accountRelatedHTMLFilesFolder + "change-password";
		}
		
		customLogger.loggerInfoWithHttpServletRequestAndMessageParam(request, "Add flash attribute \"userAccountPasswordChanged\" and redirect to" + ROOT_OF_CLASS + PATH_LOGIN);
		redirectAttributes.addFlashAttribute("userAccountPasswordChanged", true);
		return "redirect:" + ROOT_OF_CLASS + PATH_LOGIN;
	}
}
