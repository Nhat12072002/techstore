package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest()
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand1() {//add 1 category vào brand
		Category sieuxe=new Category(37);
		Brand audi = new Brand("Audi R8");
		audi.getCategories().add(sieuxe);
		
		Brand savedBrand=repo.save(audi);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateBrand2() {//add 2 hoặc nhiều category vào brand
		Category xemay=new Category(34);
		Category xedap=new Category(33);
		Brand vinfast = new Brand("Vinfast");
		vinfast.getCategories().add(xemay);
		vinfast.getCategories().add(xedap);
		Brand savedBrand=repo.save(vinfast);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateBrand3() {//add 1 brand vào category
		Brand merc = new Brand("Mercerdes");
		merc.getCategories().add(new Category(50));
		merc.getCategories().add(new Category(37));
		Brand savedBrand=repo.save(merc);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testFindAll() {
		Iterable<Brand> brands=repo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	@Test
	public void testGetById() {
		Brand brands=repo.findById(3).get();
		
		
		assertThat(brands.getName()).isEqualTo("Audi R8");
	}
	@Test
	public void testUpdateName() {
		String newName="Audi";
		Brand brands=repo.findById(3).get();
		brands.setName(newName);
		Brand savedBrand=repo.save(brands);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	@Test
	public void testDelete() {
		Integer id=3;
		repo.deleteById(id);
		Optional<Brand> result=repo.findById(id);
		assertThat(result.isEmpty());
	}
	}

