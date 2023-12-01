package com.shopme.common.entity;


import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "districts")
public class District extends IdBasedEntity {
	@Column(nullable = false, length = 45)
	private String name;
	@Column(nullable = false, length = 5)
	private String code;	
	@OneToMany(mappedBy = "district")
	private Set<Ward> wards;
	public District() {		
	}	
	public District(Integer id) {
		this.id = id;
	}
	public District(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	public District(String name, String code) {
		this.name = name;
		this.code = code;
	}
	public District(String name) {
		this.name = name;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "District [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
}