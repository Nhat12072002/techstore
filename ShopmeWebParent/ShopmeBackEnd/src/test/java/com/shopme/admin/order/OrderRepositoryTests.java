package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired private OrderRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testAddNewOrder() {
		Customer customer = entityManager.find(Customer.class, 15);
		Product product = entityManager.find(Product.class, 2);
		
		Order mainOrder = new Order();
		mainOrder.setCustomer(customer);
		mainOrder.setFirstname(customer.getFirstname());
		mainOrder.setLastname(customer.getLastname());
		mainOrder.setPhoneNumber(customer.getPhoneNumber());;
		mainOrder.setAddress(customer.getAddress());
		
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice()+10);
		
		mainOrder.setPaymentMethod(PaymentMethod.COD);
		mainOrder.setOrderStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);
		
		OrderDetail orderDetail= new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(3);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail);
		Order savedOrder=repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
}
