package com.albiontools.security.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.albiontools.security.account.model.User;


@Service
public class EmailSenderServiceImpl implements EmailSenderService{
	
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	

	@Value("${emailSenderService.emailFrom}")
	private String emailFrom;
	
	@Value("${emailSenderService.verificationEmail.subject}")
	private String verificationEmailSubject;	
	@Value("${emailSenderService.forgotPasswordEmail.subject}")
	private String forgotPasswordEmailSubject;

	@Value("${emailSenderService.verificationEmail.text}")
	private String verificationEmailText;
	@Value("${emailSenderService.forgotPasswordEmail.text}")
	private String forgotPasswordEmailText;
	
	@Async
	@Override
	public void sendVerificationEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(verificationEmailSubject);
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(verificationEmailText + user.getConfirmationToken().getConfirmationToken());
        javaMailSender.send(mailMessage);
	}
	
	@Async
	@Override
	public void sendForgotPasswordEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(forgotPasswordEmailSubject);
        mailMessage.setFrom(emailFrom);
        mailMessage.setText(forgotPasswordEmailText + user.getConfirmationToken().getConfirmationToken());

        javaMailSender.send(mailMessage);
	}
	
	
}
