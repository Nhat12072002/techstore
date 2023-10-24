package com.shopme;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Settings;
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
    public String viewHomePage(Model model) {
        List<Category> listCategories = categoryService.listParentCategories();
        List<Product> listProducts = productService.listAll();
        List<Category> listChildrenCategories = categoryService.listParentCategories();
        List<Settings> listThumbnails= settingService.listThumbnail();
        model.addAttribute("listThumbnails",listThumbnails);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listChildrenCategories", listChildrenCategories);
        return "index";
    }
}
	

