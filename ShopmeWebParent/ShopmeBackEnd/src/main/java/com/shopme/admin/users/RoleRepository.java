package com.shopme.admin.users;

import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;

import org.springframework.data.repository.*;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

}
