package com.shopme.customer;

	import java.io.UnsupportedEncodingException;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.repository.query.Param;
	import org.springframework.mail.javamail.JavaMailSender;
	import org.springframework.mail.javamail.JavaMailSenderImpl;
	import org.springframework.mail.javamail.MimeMessageHelper;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PostMapping;

	import com.shopme.common.entity.Customer;
	import com.shopme.customer.CustomerNotFoundException;
	import com.shopme.settings.Utilities;
	import jakarta.mail.MessagingException;
	import jakarta.mail.internet.MimeMessage;
	import jakarta.servlet.http.HttpServletRequest;

	@Controller
	public class ForgotPasswordController {
		@Autowired private CustomerService customerService;
		@Autowired JavaMailSender mailSender;


		@GetMapping("/forgot_password")
		public String showRequestForm() {
			return "customer/forgot_password_form";
		}

		@PostMapping("/forgot_password")
		public String processRequestForm(Customer customer, HttpServletRequest request, Model model) {
			String email = request.getParameter("email");
			try {
				String token = customerService.updateResetPasswordToken(email);
				String link = Utilities.getSiteURL(request) + "/reset_password?token=" + token;
				sendEmail(customer, link, email);

				model.addAttribute("message", "Chúng tôi đã gửi liên kết đặt lại mật khẩu đến email của bạn. "
						+ " Vui lòng kiểm tra.");
			} catch (CustomerNotFoundException e) {
				model.addAttribute("error", e.getMessage());
			} catch (UnsupportedEncodingException | MessagingException e) {
				model.addAttribute("error", "Không thể gửi email");
			}

			return "customer/forgot_password_form";
		}

		private void sendEmail(Customer customer, String link, String email) 
				throws UnsupportedEncodingException, MessagingException {
			String subject = "Reset Your Password";
			String senderName ="TechStore";
			String content = "<p>Xin chào,</p>"
					+ "<p>Bạn vừa gửi yêu cầu đổi mật khẩu.</p>"
					+ "Nhấn vào link dưới để đổi mật khẩu:</p>"
					+ "<p><a href=\"" + link + "\">Change my password</a></p>"
					+ "<br>"
					+ "<p>Bỏ qua email này nếu bạn đã nhớ mật khẩu, "
					+ "hoặc không gửi bất kỳ yêu cầu nào.</p>";
			String verifyURL = link + "/verify?code=" + customer.getVerificationCode();
			MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);

		    helper.setFrom("marcoluan2002@gmail.com", senderName);
		    helper.setTo(customer.getEmail());
		    helper.setSubject(subject);

		    helper.setText(content, true);
		    mailSender.send(message);
		}

		@GetMapping("/reset_password")
		public String showResetForm(@Param("token") String token, Model model) {
			Customer customer = customerService.getByResetPasswordToken(token);
			if (customer != null) {
				model.addAttribute("token", token);
			} else {
				model.addAttribute("pageTitle", "Invalid Token");
				model.addAttribute("message", "Invalid Token");
				return "message";
			}

			return "customer/reset_password_form";
		}

		@PostMapping("/reset_password")
		public String processResetForm(HttpServletRequest request, Model model) {
			String token = request.getParameter("token");
			String password = request.getParameter("password");

			try {
				customerService.updatePassword(token, password);

				model.addAttribute("pageTitle", "Reset Your Password");
				model.addAttribute("title", "Reset Your Password");
				model.addAttribute("message", "Bạn đã thay đổi mật khẩu thành công");

			} catch (CustomerNotFoundException e) {
				model.addAttribute("pageTitle", "Invalid Token");
				model.addAttribute("message", e.getMessage());
			}	

			return "message";		
		}
	}