package com.shopme.admin.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;

@Repository
public interface ReviewRepository extends SearchRepository<Review, Integer>, CrudRepository<Review, Integer> {

	@Query("SELECT r FROM Review r WHERE r.headline LIKE %?1% OR "
			+ "r.comment LIKE %?1% OR r.product.name LIKE %?1% OR "
			+ "CONCAT(r.customer.firstname, ' ', r.customer.lastname) LIKE %?1%")
	public Page<Review> findAll(String keyword, Pageable pageable);

	public List<Review> findAll();
}
