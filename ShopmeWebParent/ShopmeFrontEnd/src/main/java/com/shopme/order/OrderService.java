package com.shopme.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.District;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;

@Service
public class OrderService {

	@Autowired private OrderRepository repo;
	public Order createOrder(Customer customer, District district, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		Order newOrder=new Order();
		newOrder.setOrderTime(new Date());
		newOrder.setOrderStatus(OrderStatus.NEW);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(0);
		newOrder.setTax(0);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
		newOrder.setAddress(district.getName());
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			OrderDetail orderDetail= new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost()*cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(0);
			orderDetails.add(orderDetail);
		}
		return repo.save(newOrder);
	}
	public Order createOrder1(Customer customer, String address, List<CartItem> cartItems, PaymentMethod paymentMethod, float estimatedTotal) {
		Order newOrder=new Order();
		newOrder.setOrderTime(new Date());
		newOrder.setOrderStatus(OrderStatus.NEW);
		newOrder.setProductCost(0);
		newOrder.setSubtotal(0);
		newOrder.setFirstname(customer.getFirstname());
		newOrder.setLastname(customer.getLastname());
		newOrder.setPhoneNumber(customer.getPhoneNumber());
		newOrder.setId(customer.getId());
		newOrder.setShippingCost(0);
		newOrder.setTax(0);
		
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliverDays(3);
		newOrder.setDeliverDate(new Date());
		newOrder.setAddress(customer.getAddress());
		newOrder.setCustomer(customer);
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			OrderDetail orderDetail= new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost()*cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(0);
			orderDetails.add(orderDetail);
		}
		for (CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		newOrder.setTotal(estimatedTotal);
		return repo.save(newOrder);
		
	}
}
