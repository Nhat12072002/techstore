package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> ,CrudRepository<Brand, Integer> {
public Long countById(Integer id);
public Brand findByName(String name);
@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
public Page<Brand> findAll(String keyword, Pageable pageable);
@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
public List<Brand> findAll();
}
