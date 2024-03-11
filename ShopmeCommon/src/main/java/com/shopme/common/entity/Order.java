package com.shopme.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 45, nullable = false, name = "first_name")
	private String firstname;
	@Column(length = 45, nullable = false, name = "last_name")
	private String lastname;
	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "address", length = 64)
	private String address;

	private Date orderTime;
	private float shippingCost;
	private float productCost;
	private float subtotal;
	private float tax;
	private float total;
	private int deliverDays;
	private Date deliverDate;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public void copyAddressFromCustomer() {
		setFirstname(customer.getFirstname());
		setLastname(customer.getLastname());
		setPhoneNumber(customer.getPhoneNumber());
		setAddress(customer.getAddress());
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", orderStatus="
				+ orderStatus + ", customer=" + customer + "]";
	}

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public Order(Integer id, Date orderTime, float total) {
		this.orderTime = orderTime;
		
		this.total = total;
		this.id = id;
	}

}
