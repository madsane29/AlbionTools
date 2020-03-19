package com.albiontools.security.account.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.albiontools.security.account.model.User;


@Service
public class EmailSenderServiceImpl implements EmailSenderService {
	
	@Autowired
	private MessageSource messageSource;

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	

	@Value("${emailSenderService.emailFrom}")
	private String emailFrom;
	
	private SimpleMailMessage setGeneralEmailProperties(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailFrom);
        mailMessage.setTo(user.getEmail());
		
		return mailMessage;
	}
	
	@Async
	@Override
	public void sendVerificationEmail(User user) {
        SimpleMailMessage mailMessage = setGeneralEmailProperties(user);
        mailMessage.setSubject(messageSource.getMessage("emailSenderService.verificationEmail.subject", null, LocaleContextHolder.getLocale()));
        mailMessage.setText(messageSource.getMessage("emailSenderService.verificationEmail.text", null, LocaleContextHolder.getLocale()));
        
        javaMailSender.send(mailMessage);
	}
	
	@Async
	@Override
	public void sendForgotPasswordEmail(User user) {
        SimpleMailMessage mailMessage = setGeneralEmailProperties(user);
        mailMessage.setSubject(messageSource.getMessage("emailSenderService.forgotPasswordEmail.subject", null, LocaleContextHolder.getLocale()));
        mailMessage.setText(messageSource.getMessage("emailSenderService.forgotPasswordEmail.text", null, LocaleContextHolder.getLocale()));

        javaMailSender.send(mailMessage);
	}
	
}
