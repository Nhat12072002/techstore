package com.shopme.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer>, CrudRepository<Customer, Integer>  {
	@Query("SELECT c FROM Customer c WHERE c.email =?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode= ?1")
	public Customer findByVerification(String code);
	
	@Query("UPDATE Customer c SET c.enabled=true WHERE c.id= ?1")
	@Modifying
	public void enable (Integer id);

}
