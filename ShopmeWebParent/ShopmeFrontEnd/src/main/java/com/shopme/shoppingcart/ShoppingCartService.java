package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@Service
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) {
		Integer updatedQuantity = quantity;
		Product product= new Product(productId);
		
		CartItem cartItem =cartRepo.findByCustomerAndProduct(customer, product);
		
		if(cartItem !=null) {
			updatedQuantity = cartItem.getQuantity()+quantity;
		} else {
			cartItem =new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);
		
		cartRepo.save(cartItem);
		return updatedQuantity;
	}
	public List<CartItem> listCartItems(Customer customer){
		return cartRepo.findByCustomer(customer);
	}
}
