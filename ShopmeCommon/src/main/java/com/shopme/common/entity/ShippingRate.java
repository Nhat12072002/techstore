package com.shopme.common.entity;


import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Component
@Table(name = "shipping_rates")
public class ShippingRate extends IdBasedEntity {

	private float rate;
	private int days;
	
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;
	
	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}


	public District getdistrict() {
		return district;
	}

	public void setdistrict(District district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "ShippingRate [id=" + id + ", rate=" + rate + ", days=" + days + 
				", district=" + district.getName() ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingRate other = (ShippingRate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	

	
}