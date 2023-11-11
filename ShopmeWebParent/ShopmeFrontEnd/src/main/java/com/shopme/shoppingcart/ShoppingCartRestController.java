package com.shopme.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.settings.Utilities;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer customer = getAutehnticatedCustomer(request);
			Integer updatedQuantity =cartService.addProduct(productId, quantity, customer);
			return updatedQuantity +" " + "sản phẩm đã được thêm vào giỏ hàng của bạn";
		} catch (CustomerNotFoundException ex) {
			return "Bạn phải đăng nhập để thêm sản phẩm vào giỏ.";
		}
	}

	private Customer getAutehnticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utilities.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
}
