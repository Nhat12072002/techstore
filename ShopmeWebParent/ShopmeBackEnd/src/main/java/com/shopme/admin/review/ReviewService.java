package com.shopme.admin.review;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.customer.CustomerNotFoundException;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.User;

@Service
public class ReviewService {

	public static final int REVIEWS_PER_PAGE =5;
	@Autowired private ReviewRepository repo;
	@Autowired private ProductRepository productRepo;

	public Page<Review> listByPage(int pageNum, String sortField, String sortDir,String keyword){
		Sort sort= Sort.by(sortField);
		sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();
		PageRequest pageable= PageRequest.of(pageNum - 1,REVIEWS_PER_PAGE,sort);
		if(keyword !=null) {
			return repo.findAll(keyword, pageable);
		}
		return  repo.findAll(pageable);
	}
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ReviewNotFoundException("Could not find any customers with ID " + id);
		}
	}
	public void save (Review reviewInForm) {
		Review reviewInDB= repo.findById(reviewInForm.getId()).get();
		reviewInDB.setHeadline(reviewInForm.getHeadline());
		reviewInDB.setComment(reviewInForm.getComment());
		repo.save(reviewInDB);
		productRepo.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
	}
	public void delete(Integer id) throws ReviewNotFoundException{
		if(!repo.existsById(id)) {
			throw new ReviewNotFoundException("Could not find any reviews with ID "+id);

			
		}
		productRepo.updateReviewCountAndAverageRating(id);

		repo.deleteById(id);
	}
	public List<Review> listAll() {
		
		return repo.findAll();
	}

}
