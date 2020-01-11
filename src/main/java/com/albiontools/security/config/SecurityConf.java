package com.albiontools.security.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

	
	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("petko").password("{noop}petko").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");
	}*/

	@Override
	protected void configure(HttpSecurity httpSec) {
		try {
			httpSec.csrf().disable().authorizeRequests().antMatchers("/css/*").permitAll().antMatchers("/trading")
					.authenticated().and().formLogin().loginPage("/user/login").permitAll().and().logout()
					.invalidateHttpSession(true).clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessUrl("/user/logout-success")
					.permitAll();

			httpSec.authorizeRequests().antMatchers("/h2_console/**").permitAll();
			httpSec.headers().frameOptions().disable();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
