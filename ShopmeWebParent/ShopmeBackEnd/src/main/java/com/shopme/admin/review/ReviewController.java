package com.shopme.admin.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.users.UserNotFoundException;
import com.shopme.admin.users.UserService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class ReviewController {
	@Autowired
	private ReviewService service;
	@Autowired
	private ProductService productService;

	@GetMapping("/reviews")
	public String listFirstPage(Model model) {
		List<Review> listreviews = service.listAll();
		model.addAttribute("listreviews", listreviews);
		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/reviews/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		Page<Review> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Review> listUsers = page.getContent();
		long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "reviews/reviews";
	}

	@GetMapping("/reviews/detail/{id}")
	public String viewReviews(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Review review = service.get(id);
			model.addAttribute("review", review);

			return "reviews/review_detail_modal";
		} catch (ReviewNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/reviews";
		}
	}

	@GetMapping("/reviews/delete/{id}")
	public String deleteReview(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "The review ID " + id + " has been deleted successfully.");

		} catch (ReviewNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/reviews";
	}


}
