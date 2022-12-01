package com.gym.admin.services;

import com.gym.admin.data.entities.UserEntity;
import com.gym.admin.data.payloads.security.SignupRequest;
import com.gym.admin.exceptions.EntityNotFoundException;

public interface UserService {
	
	UserEntity findByUsername(String username) throws EntityNotFoundException;
	
	UserEntity findById(Long userId) throws EntityNotFoundException;
	
	UserEntity findByUsernameWithAuthorities(String username) throws EntityNotFoundException;
	
	UserEntity registerNewUser(SignupRequest signupRequest) throws EntityNotFoundException;
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);

}
