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
import org.springframework.core.SpringVersion;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.spring5.util.SpringVersionUtils;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
public class AlbionToolsApplication /*implements CommandLineRunner*/ {

	
	// @Autowired    private ApplicationContext applicationContext;
	  
	public static void main(String[] args) {
		SpringApplication.run(AlbionToolsApplication.class, args);	
		//System.out.println("Version of Spring: " + SpringVersion.getVersion());
	}

	/*@Override
	public void run(String... args) throws Exception {
		 String[] beans = applicationContext.getBeanDefinitionNames();
		 Arrays.sort(beans);
	        for (String bean : beans) {
	            System.out.println(bean);
	        }
		
	}*/
}
