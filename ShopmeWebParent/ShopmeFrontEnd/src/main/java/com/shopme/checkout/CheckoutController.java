package com.shopme.checkout;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.settings.Utilities;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.common.entity.District;
import com.shopme.common.entity.PaymentMethod;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {
	@Autowired
	private CheckoutService checkoutService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ShippingRate shipService;
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private OrderService orderService;
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		List<District> districts = checkoutService.getAllDistricts();
		String email = Utilities.getEmailOfAuthenticatedCustomer(request);
		ShippingRate shippingRate = null;
//		if(shippingRate.getdistrict()!=null) {
//			shippingRate=shipService.setShippingCost(shippingRate.getRate());
//		} else {
//			return "redirect:/cart";
//		}
//		
		Customer customer = customerService.getCustomerByEmail(email);
		List<CartItem> cartItems = cartService.listCartItems(customer);
//		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
//
//		model.addAttribute("checkoutInfo", checkoutInfo);
		float estimatedTotal = 0.0F;
		for (CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}

		model.addAttribute("customer", customer);
		model.addAttribute("districts", districts);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);

		return "checkout/checkout";
	}

	@PostMapping("/place_order")
	public String showCheckoutSuccessPage(Model model, HttpServletRequest request) {
		String paymentType= request.getParameter("paymentMethod");
		PaymentMethod paymentMethod= PaymentMethod.COD;
		String email = Utilities.getEmailOfAuthenticatedCustomer(request);
		ShippingRate shippingRate = null;
		
		Customer customer = customerService.getCustomerByEmail(email);
		Optional<Customer> customer1=customerService.getCustomerById(customer.getId());
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		orderService.createOrder1(customer, customer.getAddress(), cartItems, paymentMethod);
		cartService.deleteByCustomer(customer);
		return "checkout/checkout_success";
	}
}


