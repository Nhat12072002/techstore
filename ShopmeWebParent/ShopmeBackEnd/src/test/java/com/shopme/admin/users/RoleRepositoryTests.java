package com.shopme.admin.users;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	private RoleRepository repo;
	 
	@Autowired
	public RoleRepositoryTests(RoleRepository repo) {
	     this.repo = repo;
	}
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin=new Role("Admin", "manage everything");
		Role saveRole=repo.save(roleAdmin);
		
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateRestRole() {
		Role roleSalesperson=new Role("Salesperson", "manage product price,"
				+"customers, shipping, order and sales report");
		Role roleEditor=new Role("Editor", "manage categories,"
				+"brands, products, articles and menus");
		Role roleShipper=new Role("Shipper", "view products,"
				+"view orders and update order status");
		Role roleAssistant=new Role("Assistant", "manage questions and reviews");
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
		
	}

}
