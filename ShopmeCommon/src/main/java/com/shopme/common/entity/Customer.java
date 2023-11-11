package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
@Entity
@Table(name="customers")
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(length=45, nullable=false, unique=true)
	private String email;
	@Column(length=64, nullable=false)
	private String password;
	@Column(length=45, nullable=false, name ="first_name")
	private String firstname;
	@Column(length=45, nullable=false, name ="last_name")
	private String lastname;
	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "address", length = 64)
	private String address;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	private boolean enabled;

	@Column(name = "created_time")
	private Date createdTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;

	@Column(name = "reset_password_token", length = 30)
	private String resetPasswordToken;
		public Customer() {
		}

		public Customer(Integer id) {
			this.id = id;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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

		public String getVerificationCode() {
			return verificationCode;
		}

		public void setVerificationCode(String verificationCode) {
			this.verificationCode = verificationCode;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public Date getCreatedTime() {
			return createdTime;
		}

		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}
		
		public AuthenticationType getAuthenticationType() {
			return authenticationType;
		}

		public void setAuthenticationType(AuthenticationType authenticationType) {
			this.authenticationType = authenticationType;
		}

		public String getResetPasswordToken() {
			return resetPasswordToken;
		}

		public void setResetPasswordToken(String resetPasswordToken) {
			this.resetPasswordToken = resetPasswordToken;
		}

		@Transient
		public String getFullName() {
			return firstname + "  " + lastname;
		}
		@Override
		public String toString() {
			return "Customer [id=" + id + ", email=" + email + ", First Name=" + firstname + "Last Name="+ lastname +"]";
		
		}

		public String setName(String firstname,String lastname) {
			return firstname + " " + lastname;
			
		}

		

	}
