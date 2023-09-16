package com.shopme.admin.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Category;
@RestController
public class ProductRestController {
	
		@Autowired 
		private ProductService service;
		
		@PostMapping("/products/check_unique")
		public String checkUnique(@Param("id")Integer id, @Param("name") String name) {
			return service.checkUnique(id, name);
		}}
		