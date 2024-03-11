package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository
		extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p WHERE p.enabled=true AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);
	@Query("SELECT p FROM Product p WHERE p.alias = ?1")
	public Product findbyAlias(String alias);
	
	@Query(value="SELECT * FROM products WHERE enabled =true AND"
			+ " MATCH (name,short_description,full_description) AGAINST (?1)",
			nativeQuery =true)
	public Page<Product> search(String keyword, Pageable pageable);
	@Modifying
	@Query("UPDATE Product p SET p.averageRating = COALESCE(CAST((SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1) AS Float), 0.0), p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) WHERE p.id = ?1")
	void updateReviewCountAndAverageRating(Integer productId);

}
