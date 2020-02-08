package com.albiontools;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class AlbionToolsApplication/* implements CommandLineRunner */{
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}
	
	// @Autowired    private ApplicationContext applicationContext;
	  
	public static void main(String[] args) {
		SpringApplication.run(AlbionToolsApplication.class, args);	
	}
/*
	@Override
	public void run(String... args) throws Exception {
		 String[] beans = applicationContext.getBeanDefinitionNames();
		 Arrays.sort(beans);
	        for (String bean : beans) {
	            System.out.println(bean);
	        }
		
	}*/
}
