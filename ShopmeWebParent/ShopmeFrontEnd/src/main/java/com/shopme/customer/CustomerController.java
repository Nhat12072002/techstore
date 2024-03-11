package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Customer;
import com.shopme.oauth.CustomerOAuth2User;
import com.shopme.security.CustomerUserDetails;
import com.shopme.settings.Utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());

		return "register/register_form";

	}

	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request) 
			throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		String siteURL = Utilities.getSiteURL(request);
		customerService.sendVerificationEmail(customer, siteURL);
		return "register/register_success";
	}

	@GetMapping("/verify")
	public String verifyAccount(String code, Model model) {
		boolean verified = customerService.verify(code);

		return "register/" + (verified ? "verify_success" : "verify_fail");
	}


	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = Utilities.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);
		model.addAttribute("customer", customer);
		return "customer/account_form";
	}

	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {
		customerService.update(customer);
		ra.addFlashAttribute("message", "Your account details have been updated.");

		updateNameForAuthenticatedCustomer(customer, request);

		return "redirect:/account_details";
	}

	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();

		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setName(customer.getFirstname(), customer.getLastname());

		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			oauth2User.setFullName(customer.getFirstname(), customer.getLastname());
		}		
	}

	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}

		return userDetails;
	}
}