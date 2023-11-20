package com.shopme.checkout;

import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {
	private float productCost;
	private float productTotal;
	private float shippingCostTotal;
	private float paymentTotal;
	private int deliverDays;
	private Date deliverDate;
	public float getProductCost() {
		return productCost;
	}
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	public float getProductTotal() {
		return productTotal;
	}
	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}
	public float getShippingCostTotal() {
		return shippingCostTotal;
	}
	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}
	public float getPaymentTotal() {
		return paymentTotal;
	}
	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	public int getDeliverDays() {
		return deliverDays;
	}
	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}
	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, deliverDays);
		return deliverDate;
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

}
