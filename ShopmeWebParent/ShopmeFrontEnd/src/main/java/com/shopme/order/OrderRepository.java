package com.shopme.order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Order;
public interface OrderRepository extends JpaRepository<Order, Integer> {

	
}
