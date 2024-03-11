package com.shopme.product;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=6;
	public static final int MAINPRODUCTS_PER_PAGE=9;

	public static final int SEARCH_PRODUCTS_PER_PAGE=6;

	@Autowired private ProductRepository repo;

	public Page<Product> listByCategory(int pageNum,Integer categoryId){
		String categoryIdMatch="-" + String.valueOf(categoryId) + "-"	;
		PageRequest pageable =PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE );
		
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	public Page<Product> listAll(int pageNum) {
		PageRequest pageable =PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE );
        return  repo.findAll(pageable);
    }
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product=repo.findbyAlias(alias);
		if(product == null) {
			throw new ProductNotFoundException("Could not find any product.");
		}
		return product;
	}
	public Page<Product> searchProduct(String keyword, int pageNum){
		PageRequest pageable =PageRequest.of(pageNum-1,SEARCH_PRODUCTS_PER_PAGE );
		return repo.search(keyword, pageable);
	}
	public Product getProduct(Integer id) throws ProductNotFoundException {
		try {
		Product product=repo.findById(id).get();
		return product;
		}catch (NoSuchElementException ex){
			throw new ProductNotFoundException("Could not find any product with ID"+id);
		}
	}
}
