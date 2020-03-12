package com.albiontools.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private LogoutHandler logoutHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity httpSec) {
		try {
			httpSec
				.authorizeRequests()
					.antMatchers("resources/**").permitAll()
					.antMatchers("/admin/**").denyAll()
					.antMatchers("/trading/**").permitAll()
					.antMatchers("/banking").hasAnyRole("USER", "ADMIN")
				.and()
					.formLogin()
						.failureHandler(authenticationFailureHandler)
						.loginPage("/user/login").permitAll()
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
						.addLogoutHandler(logoutHandler)
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
						.logoutSuccessUrl("/user/logout-success").permitAll()
				.and()
					.rememberMe();
			
			httpSec.csrf().disable();
			//httpSec.authorizeRequests().antMatchers("/h2_console/**").permitAll();
			//httpSec.headers().frameOptions().disable();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
