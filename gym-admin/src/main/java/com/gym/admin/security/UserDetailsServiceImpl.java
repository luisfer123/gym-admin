package com.gym.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gym.admin.data.entities.UserEntity;
import com.gym.admin.services.UserService;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity user = null;
		
		try {
			System.out.println("trying to find user with username " + username);
			user = userService.findByUsernameWithAuthorities(username);
			System.out.println("user found");
		} catch (Exception e) {
			System.out.println("Exception thrown.");
			throw new UsernameNotFoundException("No user with username: " + username + " was found.");
		}
		
		return UserDetailsImpl
				.build(user);
	}

}
