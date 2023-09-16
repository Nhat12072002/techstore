package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.users.UserRepository;
import com.shopme.common.entity.User;

public class ShopmeUserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=userRepo.getUserByEmail(email);
		if(user!=null) {
			return new ShopmeUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email:" +email);
	}

}
