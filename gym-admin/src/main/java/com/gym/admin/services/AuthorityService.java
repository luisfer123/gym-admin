package com.gym.admin.services;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.enums.ERole;
import com.gym.admin.exceptions.EntityNotFoundException;

public interface AuthorityService {
	
	AuthorityEntity findByRole(ERole role) throws EntityNotFoundException;

}
