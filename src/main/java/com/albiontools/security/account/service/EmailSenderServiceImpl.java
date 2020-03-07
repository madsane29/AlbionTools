package com.albiontools.security.account.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.albiontools.security.account.model.User;


@Service
public class EmailSenderServiceImpl implements EmailSenderService{
	
	@Autowired
	private MessageSource messageSource;

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	

	@Value("${emailSenderService.emailFrom}")
	private String emailFrom;
	
	@Async
	@Override
	public void sendVerificationEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(messageSource.getMessage("emailSenderService.verificationEmail.subject", null, LocaleContextHolder.getLocale()));
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(messageSource.getMessage("emailSenderService.verificationEmail.text", null, LocaleContextHolder.getLocale()));
        
        javaMailSender.send(mailMessage);
	}
	
	@Async
	@Override
	public void sendForgotPasswordEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(messageSource.getMessage("emailSenderService.forgotPasswordEmail.subject", null, LocaleContextHolder.getLocale()));
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(messageSource.getMessage("emailSenderService.forgotPasswordEmail.text", null, LocaleContextHolder.getLocale()));

        javaMailSender.send(mailMessage);
	}
	
}
