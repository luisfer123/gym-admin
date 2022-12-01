package com.gym.admin.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.entities.UserEntity;
import com.gym.admin.data.enums.ERole;
import com.gym.admin.data.payloads.security.SignupRequest;
import com.gym.admin.exceptions.EntityNotFoundException;
import com.gym.admin.repositories.UserRepository;
import com.gym.admin.services.AuthorityService;
import com.gym.admin.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	@Transactional(readOnly = true)
	public UserEntity findByUsername(String username) throws EntityNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException("User Entity with username: " + username + " was not found."));
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize(value = "hasRole('ADMIN') or principal.id == #userId")
	public UserEntity findById(Long userId) throws EntityNotFoundException {
		return userRepo.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("UserEntity with id " + userId + " does not exists."));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserEntity findByUsernameWithAuthorities(String username)
			throws EntityNotFoundException {
		
		return userRepo.findByUsernameWithAuthorities(username)
				.orElseThrow(() -> new EntityNotFoundException("User Entity with username: " + username + " was not found."));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	// @PreAuthorize(value = "hasRole('ADMIN')")
	public UserEntity registerNewUser(SignupRequest signupRequest) 
			throws EntityNotFoundException {
		
		UserEntity user = new UserEntity(signupRequest.getUsername(),
				encoder.encode(signupRequest.getPassword()),
				signupRequest.getEmail());
		
		Set<String> strRoles = signupRequest.getRoles();
		Set<AuthorityEntity> authorities = new HashSet<>();
		
		if(strRoles == null || strRoles.size() == 0) {
			AuthorityEntity userAuth = 
					authorityService.findByRole(ERole.ROLE_USER);
			authorities.add(userAuth);
		} else {
			if(!strRoles.contains("user")) {
				// Here we could instead response with BadRequest code and do not
				// register the new user.
				AuthorityEntity userAuth = 
						authorityService.findByRole(ERole.ROLE_USER);
				authorities.add(userAuth);
			}
			
			strRoles.forEach(role -> {
				switch(role) {
				// If more Roles are added, add here the corresponding cases
				case "admin":
					authorities.add(authorityService.findByRole(ERole.ROLE_ADMIN));
					break;
				case "member":
					authorities.add(authorityService.findByRole(ERole.ROLE_MEMBER));
					break;
				case "coach":
					authorities.add(authorityService.findByRole(ERole.ROLE_COACH));
					break;
				case "user":
					authorities.add(authorityService.findByRole(ERole.ROLE_USER));
					break;
				}
			});
		}
		
		user.setAuthorities(authorities);
		
		return userRepo.save(user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}

}
