package com.albiontools.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.albiontools.security.account.handler.CustomAuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

	
	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
	
	
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("petko").password("{noop}petko").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");
	}*/

	@Override
	protected void configure(HttpSecurity httpSec) {
		try {
			httpSec.csrf().disable()
			.authorizeRequests()
				.antMatchers("/trading").authenticated()
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
					.and().rememberMe()
					;
					
				;

			httpSec.authorizeRequests().antMatchers("/h2_console/**").permitAll();
			httpSec.headers().frameOptions().disable();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
