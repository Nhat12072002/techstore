package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review extends IdBasedEntity {
	@Column(nullable = false, length = 128)
	private String headline;
	@Column(nullable = false, length = 300)
	private String comment;
	private int rating;
	@Column(nullable = false)
	private Date reviewTime;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name ="product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name ="customer_id")
	private Customer customer;
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Date getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@Override
	public String toString() {
		return "Review [headline=" + headline + ", rating=" + rating + ", reviewTime=" + reviewTime + ", product="
				+ product + ", customer=" + customer + "]";
	}
	
}
