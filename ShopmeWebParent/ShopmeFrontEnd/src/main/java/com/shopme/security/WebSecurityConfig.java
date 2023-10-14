package com.shopme.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
	    .authorizeRequests()
	        .requestMatchers("/Techstore").permitAll()
	    .and()
	    .formLogin()
	        .loginPage("/login")
	        .permitAll()
	    .and()
	    .logout()
	        .permitAll();
	    return http.build();
	}

	
	
}
