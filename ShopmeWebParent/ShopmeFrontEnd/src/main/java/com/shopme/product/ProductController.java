package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryNotFoundException;
import com.shopme.category.CategoryService;
import com.shopme.product.ProductService;
import com.shopme.review.ReviewService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Review;

@Controller
public class ProductController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ReviewService reviewService;

	@GetMapping("/category/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategoryByPage(alias, 1, model);
	}

	@GetMapping("/category/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum,
			Model model) {
		List<Category> listCategories = categoryService.listParentCategories();
		try {
			Category category = categoryService.getCategory(alias);

			List<Category> listCategoryParents = categoryService.getCategoryParents(category);
			Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProduct.getContent();

			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if (endCount > pageProduct.getTotalElements()) {
				endCount = pageProduct.getTotalElements();
			}
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPage", pageProduct.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProduct.getTotalElements());
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("category", category);

			return "product/products_by_category";
		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/product/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
		List<Category> listCategories = categoryService.listParentCategories();

		try {
			Product product=productService.getProduct(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			Page<Review> listReviews = reviewService.list3MostRecentReviewsByProduct(product);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getName());
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listReviews", listReviews);

			model.addAttribute("listCategories", listCategories);

			return "product/product_detail";
		} catch(ProductNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchProductFirstPage(@Param("keyword") String keyword, Model model) {
		return searchProductByPage(keyword, 1, model);
	}
	@GetMapping("/search/page/{pageNum}")
	public String searchProductByPage(@Param("keyword") String keyword, 
			@PathVariable("pageNum") int pageNum,Model model) {
		Page<Product> pageProducts =productService.searchProduct(keyword, pageNum);
		List<Product> listResult=pageProducts.getContent();
		
		long startCount = (pageNum - 1) * ProductService.SEARCH_PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_PRODUCTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPage", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("keyword", keyword);
		model.addAttribute("listResult", listResult);
		model.addAttribute("pageTitle", keyword + "- Kết quả tìm kiếm");

		return "product/search_result";
	}
}
