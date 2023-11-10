package com.shopme.oauth;

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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String clientName = oauth2User.getClientName();
		System.out.println("OAuth2LoginSuccessHandler: " + name + " | " + email);
		System.out.println("Client Name: " + clientName);
		AuthenticationType authenticationType =getAuthenticationType(clientName);
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email,authenticationType);
		} else {
			oauth2User.setFullName(customer.getFirstname(), customer.getLastname());
			customerService.updateAuthenticationType(customer, authenticationType);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
	private AuthenticationType getAuthenticationType (String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
	}
}