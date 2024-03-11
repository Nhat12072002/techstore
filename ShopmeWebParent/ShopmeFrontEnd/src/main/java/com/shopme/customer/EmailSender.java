package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class EmailSender {

    @Autowired
    private JavaMailSender emailSender;

    public void sendOrderConfirmationEmail(String to, Order order) throws UnsupportedEncodingException, MessagingException {
        String subject = "Xác nhận đơn hàng";
        String senderName = "TechStore";

        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<html><body>");
        mailContent.append("<p>Xin chào ").append(order.getCustomer().getFullName()).append(", </p>");
        mailContent.append("<p>Cảm ơn quý khách đã đặt sản phẩm tại cửa hàng của chúng tôi.</p>");
        mailContent.append("<p>Đơn hàng của bạn đã được đặt thành công. Mã đơn hàng: ").append(order.getId()).append("</p>");

        // Product details
        mailContent.append("<p>Thông tin đơn hàng:</p>");
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            mailContent.append("<p>- ").append(orderDetail.getProduct().getName()).append(": ").append(orderDetail.getQuantity()).append(" x ").append(orderDetail.getUnitPrice()).append(" VND</p>");
        }
        mailContent.append("<p>Tổng cộng: ").append(order.getTotal()).append(" triệu VND</p>");

        // Closing message
        mailContent.append("<p>Cảm ơn bạn đã mua sắm tại TechStore.</p>");
        mailContent.append("<p>Thân ái,<br>Đội ngũ TechStore</p>");

        mailContent.append("</body></html>");

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("minhnhatnguyenphan1207@gmail.com", senderName);
        helper.setTo(order.getCustomer().getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent.toString(), true);

        emailSender.send(message);
    }
}

