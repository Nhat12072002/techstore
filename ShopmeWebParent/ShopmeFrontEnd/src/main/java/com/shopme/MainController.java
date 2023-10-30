package com.shopme;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryNotFoundException;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Settings;
import com.shopme.product.ProductNotFoundException;
import com.shopme.product.ProductService;
import com.shopme.thumbnails.SettingService;
@Controller
public class MainController {
	@Autowired private CategoryService categoryService;
	@Autowired
    private ProductService productService;
	@Autowired
	private SettingService settingService;
	@GetMapping("")
	public String viewHomePage(Model model) throws ProductNotFoundException {
		return viewHomePagePagination(model,1);
	}
	@GetMapping("/page/{pageNum}")
    public String viewHomePagePagination(Model model, @PathVariable("pageNum") int pageNum) throws ProductNotFoundException {
        List<Category> listCategories = categoryService.listParentCategories();
        Page<Product> listProducts = productService.listAll(pageNum);
        List<Category> listChildrenCategories = categoryService.listParentCategories();
        List<Settings> listThumbnails= settingService.listThumbnail();

        long startCount = (pageNum - 1) * ProductService.MAINPRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.MAINPRODUCTS_PER_PAGE - 1;
		if (endCount > listProducts.getTotalElements()) {
			endCount = listProducts.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPage", listProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", listProducts.getTotalElements());
        model.addAttribute("listThumbnails",listThumbnails);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listChildrenCategories", listChildrenCategories);
        return "index";
	}
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}

		return "redirect:/";
	}	
}
	

