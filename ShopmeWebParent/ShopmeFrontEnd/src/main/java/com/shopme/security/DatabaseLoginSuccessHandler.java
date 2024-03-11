package com.shopme.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
		Customer customer = userDetails.getCustomer();

		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);

		super.onAuthenticationSuccess(request, response, authentication);
	}


}