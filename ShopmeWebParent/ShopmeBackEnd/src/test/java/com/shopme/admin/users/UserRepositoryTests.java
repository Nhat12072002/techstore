package com.shopme.admin.users;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUser_OneRole() {
		Role roleAdmin= entityManager.find(Role.class,1 );
		User userNhat=new User("minhnhatnguyenphan1207@gmail.com","Nhat1207","Nhat","Nguyen Phan Minh");
		userNhat.addRole(roleAdmin);
		User saveUser = repo.save(userNhat);
	assertThat(saveUser.getId()).isGreaterThan(0);
	
	}
	@Test
	public void testCreateNewUser_TwoRole() {
		User userLuan = new User("luandinhvo2002@gmail.com","Luan2002","Luan","Vo Dinh");
		Role roleEditor= new Role(3);
		Role roleAssistant= new Role(5);
		userLuan.addRole(roleEditor);
		userLuan.addRole(roleAssistant);
		User savedUser =repo.save(userLuan);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		Iterable<User> listUser= repo.findAll();
		listUser.forEach(user -> System.out.println(user));
		
	}
	@Test
	public void testGetUserById() {
		User userNhat=repo.findById(1).get();
		System.out.println(userNhat);
		assertThat(userNhat).isNotNull();
	}
	@Test
	public void testUpdateUserDetails() {
		User userNhat=repo.findById(34).get();
		Role roleAdmin= new Role(1);
		userNhat.addRole(roleAdmin);
		repo.save(userNhat);
	}
	@Test
	public void testUpdateRole() {
		User userLuan=repo.findById(2).get();
		Role roleEditor= new Role(3);
		Role roleSalesPerson=new Role(2);
		userLuan.getRoles().remove(roleEditor);
		userLuan.addRole(roleSalesPerson);
		repo.save(userLuan);
	}
	@Test
	public void testDeleteUser() {
		Integer userId=2;
		repo.deleteById(userId);
	}
	@Test
	public void testGetUserByEmail() {
		String email="minhnhatnguyenphan1207@gmail.com";
		User user=repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	@Test
	public void testCountById() {
		Integer id=1;
		Long countById= repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	@Test
	public void testDisableUser() {
		Integer id=1;
		repo.updateEnabledStatus(id, false);
	}
	@Test
	public void testEnableUser() {
		Integer id=1;
		repo.updateEnabledStatus(id, true);
	}
	@Test
	public void testListFirstPage() {
		int pageNumber=0;
		int pageSize=4;
		PageRequest pageable=PageRequest.of(pageNumber,pageSize);
		Page<User> page=repo.findAll(pageable);
		List<User> listUsers=page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	@Test
	public void testSearchUsers() {
		String keyword ="bruce";
		
		int pageNumber=0;
		int pageSize=4;
		PageRequest pageable=PageRequest.of(pageNumber,pageSize);
		Page<User> page=repo.findAll(keyword, pageable);
		List<User> listUsers=page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0);
		
	}
}
