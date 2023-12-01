package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	@Autowired private ProductRepository productRepo;
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
	
	public float updateQuantity (Integer productId, Integer quantity, Customer customer) {
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepo.findById(productId).get();
	    float subtotal = product.getDiscountPrice()*quantity;
	    return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartRepo.deleteByCustomerAndProduct(customer.getId(),productId);
	}
	public void deleteByCustomer(Customer customer) {
		cartRepo.deleteByCustomer(customer.getId());
	}
}
