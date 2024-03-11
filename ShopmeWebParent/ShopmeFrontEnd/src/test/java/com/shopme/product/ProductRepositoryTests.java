package com.shopme.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.category.CategoryRepository;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace =Replace.NONE)
public class ProductRepositoryTests {
	@Autowired private ProductRepository repo;
	@Test
	public void testFindbyAlias() {
		String alias="LaptopGaming";
		Product product=repo.findbyAlias(alias);
		
		assertThat(product).isNotNull();
	}
}
