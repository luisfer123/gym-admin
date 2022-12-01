package com.gym.admin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.enums.ERole;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
	
	Optional<AuthorityEntity> findByRole(ERole role);

}
