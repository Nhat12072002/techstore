package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerNotFoundException;

import net.bytebuddy.utility.RandomString;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

	@Autowired private CustomerRepository customerRepo;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired JavaMailSender mailSender;


	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	public Customer registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		return customerRepo.save(customer);

	}

	public void sendVerificationEmail(Customer customer, String siteURL) throws UnsupportedEncodingException, MessagingException {
		String subject = "Please verify your registration";
		String senderName ="TechStore";
		String mailContent = "<p>Dear "+ customer.getFullName() + ", </p>";
		mailContent += "<p>Quý khách vui lòng nhấn vào link bên dưới để xác thực tài khoản.";
		String verifyURL = siteURL + "/verify?code=" + customer.getVerificationCode();

		mailContent += "<h3><a href=\"" + verifyURL +"\">VERIFY </a></h3>";
		mailContent += "<p>Thank you<br> TechStore Team</p>";

		MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);

	    helper.setFrom("minhnhatnguyenphan1207@gmail.com", senderName);
	    helper.setTo(customer.getEmail());
	    helper.setSubject(subject);

//	    mailSender = mailSender.replace("[[name]]", customer.getName());
//	    String verifyURL = siteURL + "/verify?code=" + customer.getVerificationCode();
//	     
//	    mailSender = mailSender.replace("[[URL]]", verifyURL);
//	     
	    helper.setText(mailContent, true);

	    mailSender.send(message);
	}
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	public Optional<Customer> getCustomerById(Integer id) {
		return customerRepo.findById(id);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}

	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);

		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddress("");
		customer.setPhoneNumber("");		
		customerRepo.save(customer);
	}	


		private void setName(String name, Customer customer) {
			String[] nameArray = name.split(" ");
			if (nameArray.length < 2) {
				customer.setFirstname(name);
				customer.setLastname(" ");
			} else {
				String firstName = nameArray[0];
				customer.setFirstname(firstName);
				
				String lastName = name.replaceFirst(firstName + " ", " ");
				customer.setLastname(lastName);
			}
		}


	public void update(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

		if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
				customerInForm.setPassword(encodedPassword);			
			} else {
				customerInForm.setPassword(customerInDB.getPassword());
			}		
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}

		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

		customerRepo.save(customerInForm);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);

			return token;
		} else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
	}	

	public Customer getByResetPasswordToken(String token) {
		return customerRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if (customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}

		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		encodePassword(customer);

		customerRepo.save(customer);
	}
	public void getEmailSettings(Customer customer, String siteURL) throws UnsupportedEncodingException, MessagingException {
		String subject = "Reset Your Password";
		String senderName ="TechStore";
		String content = "<p>Xin chào,</p>"
				+ "<p>Bạn vừa gửi yêu cầu đổi mật khẩu.</p>"
				+ "Nhấn vào link dưới để đổi mật khẩu:</p>"
				+ "<p><a href=\"" + siteURL + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p>Bỏ qua email này nếu bạn đã nhớ mật khẩu, "
				+ "hoặc không gửi bất kỳ yêu cầu nào.</p>";
		String verifyURL = siteURL + "/verify?code=" + customer.getVerificationCode();
		MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);

	    helper.setFrom("minhnhatnguyenphan1207@gmail.com", senderName);
	    helper.setTo(customer.getEmail());
	    helper.setSubject(subject);

	    helper.setText(content, true);
	    mailSender.send(message);


	}
}