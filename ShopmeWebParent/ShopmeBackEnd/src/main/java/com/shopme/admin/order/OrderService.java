package com.shopme.admin.order;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Product;

@Service
public class OrderService {

	static final int ORDER_PER_PAGE=4;
	@Autowired private OrderRepository repo;
	
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();
		 Pageable pageable=PageRequest.of(pageNum-1,ORDER_PER_PAGE,sort);
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	public Order get(Integer id) throws OrderNotFoundException{
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any orders with ID "+ id);
		}
	}
	public void delete(Integer id) throws OrderNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new OrderNotFoundException("Could not find any orders with ID: " + id);
		}
		repo.deleteById(id);
	}
	public Order save(Order order) {
		return repo.save(order);
		
	}
}
