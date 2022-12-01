package com.gym.admin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gym.admin.data.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	@Query(value = "select u from UserEntity u join fetch u.authorities where u.username = :username",
			countQuery = "select count(u) from UserEntity u join fetch u.authorities where u.username = :username")
	Optional<UserEntity> findByUsernameWithAuthorities(@Param("username") String username);
	
	Optional<UserEntity> findByUsername(String username);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);

}
