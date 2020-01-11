package com.albiontools.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("petko").password("{noop}petko").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");
	}

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
