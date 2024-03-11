package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE=4;
	@Autowired
	private BrandRepository repo;
	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
	}
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	public Brand get(Integer id) throws BrandNotFoundException{
		try {
			return repo.findById(id).get();
		} catch(NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID" + id);
			}
		}
		public void delete(Integer id) throws BrandNotFoundException{
			Long countById=repo.countById(id);
			if(countById == null || countById == 0) {
				throw new BrandNotFoundException("Could not find any brand with ID: " +id);
			}
			repo.deleteById(id);
		
	}
		public String checkUnique(Integer id, String name) {
			boolean isCreatingNew= (id==null||id==0);
			Brand brandByName=repo.findByName(name);
			if(isCreatingNew) {
				if(brandByName!=null){
					return "Duplicate";
				}
			}else {
				if(brandByName!=null && brandByName.getId()!=id) 
					return "Duplicate";
			}	
			return "OK";
		}
		public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword){
			Sort sort=Sort.by(sortField);
			sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();
			 Pageable pageable=PageRequest.of(pageNum-1,BRANDS_PER_PAGE,sort);
			if (keyword != null) {
				return repo.findAll(keyword, pageable);
			}
			return repo.findAll(pageable);
		}
}
