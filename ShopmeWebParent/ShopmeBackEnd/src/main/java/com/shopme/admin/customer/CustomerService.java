package com.shopme.admin.customer;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopme.common.entity.Customer;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;

	@Autowired private CustomerRepository customerRepo;
	@Autowired private PasswordEncoder passwordEncoder;

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepo.findAll(keyword, pageable);
		}

		return customerRepo.findAll(pageable);
	}

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
	}



	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			// found another customer having the same email
			return false;
		}

		return true;
	}

	public void save(Customer customerInForm) {
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
		} else {
			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
			customerInForm.setPassword(customerInDB.getPassword());
		}		
		customerRepo.save(customerInForm);
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}

		customerRepo.deleteById(id);
	}

}