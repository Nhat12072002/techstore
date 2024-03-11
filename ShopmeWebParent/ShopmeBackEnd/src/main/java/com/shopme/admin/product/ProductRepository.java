package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository
		extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {
	public Product findByName(String name);

	@Query("UPDATE Product p Set p.enabled = ?2 WHERE p.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Long countById(Integer id);

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" + "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%" + "OR p.brand.name LIKE %?1%" + "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

	@Modifying
	@Query("UPDATE Product p SET p.averageRating = COALESCE(CAST((SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1) AS Float), 0.0), p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) WHERE p.id = ?1")
	void updateReviewCountAndAverageRating(Integer productId);

}
