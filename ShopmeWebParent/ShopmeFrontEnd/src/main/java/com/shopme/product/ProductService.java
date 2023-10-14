package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=4;
	
	@Autowired private ProductRepository repo;

	public Page<Product> listByCategory(int pageNum,Integer categoryId){
		String categoryIdMatch="-" + String.valueOf(categoryId) + "-"	;
		PageRequest pageable =PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE );
		
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	public List<Product> listAll() {
        return (List<Product>) repo.findAll();
    }
}
