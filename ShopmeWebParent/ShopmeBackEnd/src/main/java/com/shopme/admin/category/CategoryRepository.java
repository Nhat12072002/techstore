package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>,CrudRepository<Category, Integer> {
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageble);
	public Long countById(Integer id);
	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	@Transactional
	public void updateEnabledStatus(Integer id, boolean enabled);
	public Category findByName(String name);
	public Category findByAlias(String alias);
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	public Page<Category> search(String keyword, Pageable pageble);
}
