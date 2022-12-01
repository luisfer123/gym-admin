package com.gym.admin.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.enums.ERole;
import com.gym.admin.exceptions.EntityNotFoundException;
import com.gym.admin.repositories.AuthorityRepository;
import com.gym.admin.services.AuthorityService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	private final AuthorityRepository authRepo;
	
	@Override
	@Transactional
	public AuthorityEntity findByRole(ERole role) throws EntityNotFoundException {
		return authRepo.findByRole(role)
				.orElseThrow(() -> new EntityNotFoundException("AuthorityEntity with role: " + role + " was not found."));
	}

}
