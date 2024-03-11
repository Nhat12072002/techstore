package com.shopme.admin.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Product;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>, CrudRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.firstname LIKE %?1% OR"
			+ " o.lastname LIKE %?1% OR o.phoneNumber LIKE %?1% OR" + " o.address LIKE %?1% OR"
			+ " o.paymentMethod LIKE %?1% OR o.orderStatus LIKE %?1% OR" + " o.firstname LIKE %?1% OR"
			+ " o.lastname LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);

	public Long countById(Integer id);

	@Query("SELECT NEW com.shopme.common.entity.Order(o.id, o.orderTime, o.total) FROM Order o WHERE o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
	public List<Order> findByOrderTimeDone(Date startTime, Date endTime);
}
