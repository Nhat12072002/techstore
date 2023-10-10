package com.shopme.admin.security;

import java.util.Collection;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public class ShopmeUserDetails implements UserDetails {

	
	
	private static final long serialVersionUID = 1L;
	private User user;
	
	
	public ShopmeUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles=user.getRoles();
		
		List<SimpleGrantedAuthority> authories= new ArrayList();
		for( Role role:roles) {
			authories.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authories;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}
	public String getFullname() {
		return this.user.getFirstname()+" "+user.getLastname();
	}
	public void setFirstname(String firstname) {
		this.user.setFirstname(firstname);
	}
	public void setLastname(String lastname) {
		this.user.setLastname(lastname);
	}
	public void setImageUser(String img) {
		this.user.setPhotos(img);
	}
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	
}
