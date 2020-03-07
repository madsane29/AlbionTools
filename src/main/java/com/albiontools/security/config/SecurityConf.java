package com.albiontools.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.albiontools.security.account.handler.CustomAuthenticationFailureHandler;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;


@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		//super.configure(auth);
		auth.userDetailsService(userDetailsService);
		
	}

	@Override
	protected void configure(HttpSecurity httpSec) {
		try {
			httpSec
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/trading").permitAll()//.authenticated()
				.antMatchers("/banking").hasAnyRole("USER", "ADMIN")
			.and()
				.formLogin()
					.failureHandler(customAuthenticationFailureHandler())
					.loginPage("/user/login").permitAll()
					.and()
				.logout()
					.deleteCookies("JSESSIONID")
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
					.logoutSuccessUrl("/user/logout-success").permitAll()
				.and()
					.rememberMe();//.userDetailsService(userDetailsService);

			httpSec.authorizeRequests().antMatchers("/h2_console/**").permitAll();
			httpSec.headers().frameOptions().disable();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
