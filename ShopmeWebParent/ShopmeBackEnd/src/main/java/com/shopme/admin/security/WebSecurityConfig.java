package com.shopme.admin.security;

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
	public PasswordEncoder PasswordEndcoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // Cho phép truy cập đến tài nguyên tĩnh như CSS, JavaScript, v.v., tại các vị trí chung mà không yêu cầu xác thực.
	        // Yêu cầu xác thực cho URL "/login".
	            .requestMatchers("/users/**").hasAuthority("Admin")
	            .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
	            .requestMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
	            .requestMatchers("/products/edit/**","/products/save","/products/check_unique")
            	.hasAnyAuthority("Admin","Editor","Salesperson")

	            .requestMatchers("/products","/products/","/products/detail/**", "/products/page/**")
	            	.hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
		            .requestMatchers("/reviews/**").hasAnyAuthority("Admin","Assistant","Salesperson")

	            .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
	            .requestMatchers("/settings/**").hasAnyAuthority("Admin","Editor")
	            .requestMatchers("/customers/**","/orders/**","/reports/**").hasAnyAuthority("Admin","Salesperson")
	            .requestMatchers("/").authenticated()
	            .requestMatchers("/users").authenticated()
	            // Yêu cầu xác thực cho trang chủ.
	            .anyRequest().permitAll().and() // Cho phép truy cập vào bất kỳ URL nào khác mà không yêu cầu xác thực.
	        .formLogin()
	            .loginPage("/login") // Thiết lập trang đăng nhập tùy chỉnh với URL "/login".
	            .usernameParameter("email") // Thiết lập tên tham số cho trường nhập tên người dùng thành "email".
	    	.and().logout().permitAll()
	    	.and().rememberMe().key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 *60 *60);
	    return http.build();
	}

	@Bean 
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailService();
		
	}
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider AuthProvider= new DaoAuthenticationProvider();
		AuthProvider.setUserDetailsService(userDetailsService());
		AuthProvider.setPasswordEncoder(PasswordEndcoder());
		return AuthProvider;
	}
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
}
