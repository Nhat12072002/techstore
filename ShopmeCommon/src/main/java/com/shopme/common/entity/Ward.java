package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wards")
public class Ward extends IdBasedEntity {
	
	@Column(nullable = false, length = 45)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;

	public Ward() {
		
	}
	
	public Ward(String name, District district) {
		this.name = name;
		this.district = district;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District District) {
		this.district = District;
	}

	@Override
	public String toString() {
		return "Ward [id=" + id + ", name=" + name + "]";
	}
	
	
}