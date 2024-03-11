package com.shopme.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private int quantity;
	private float productCost;
	private float shippingCost;
	private float unitPrice;
	private float subtotal;
	
	@ManyToOne
	@JoinColumn(name= "product_id")
	private Product product;
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getProductCost() {
		return productCost;
	}
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	public float getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public OrderDetail() {
		
	}
	public OrderDetail(String categoryName, int quantity, float subtotal, Product product) {
		this.product =new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.product = product;
	}
	public OrderDetail( int quantity,String productName, float subtotal, Product product) {
		this.product =new Product(productName);
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.product = product;
	}
	
	
}
