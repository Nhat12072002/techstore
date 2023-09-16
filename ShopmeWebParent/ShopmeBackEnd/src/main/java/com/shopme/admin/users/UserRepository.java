package com.shopme.admin.users;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.*;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>,CrudRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.email= :email")
	public User getUserByEmail(@Param("email") String email);
	public Long countById(Integer id);
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstname, ' ',u.lastname) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
