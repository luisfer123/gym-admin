package com.gym.admin.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.entities.UserEntity;
import com.gym.admin.data.enums.ERole;
import com.gym.admin.repositories.AuthorityRepository;
import com.gym.admin.repositories.UserRepository;

@Service
public class DataIntializationServiceImpl {
	
	@Autowired private AuthorityRepository authRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private PasswordEncoder encoder;
	
	@Transactional
	@EventListener(classes = ApplicationReadyEvent.class)
	public void onInit() {
		addAuthoritiesAndUsers();
	}
	
	@Transactional
	public void addAuthoritiesAndUsers() {
		if(authRepo.count() == 0 && userRepo.count() == 0) {
			AuthorityEntity roleUser = new AuthorityEntity(ERole.ROLE_USER);
			AuthorityEntity roleMember = new AuthorityEntity(ERole.ROLE_MEMBER);
			AuthorityEntity roleCoach = new AuthorityEntity(ERole.ROLE_COACH);
			AuthorityEntity roleReceptionist = new AuthorityEntity(ERole.ROLE_RECEPTIONIST);
			AuthorityEntity roleAdmin = new AuthorityEntity(ERole.ROLE_ADMIN);
			
			roleUser = authRepo.saveAndFlush(roleUser);
			roleMember = authRepo.saveAndFlush(roleMember);
			roleCoach = authRepo.saveAndFlush(roleCoach);
			roleAdmin = authRepo.saveAndFlush(roleAdmin);
			roleReceptionist = authRepo.saveAndFlush(roleReceptionist);
			
			UserEntity user = UserEntity.builder()
					.username("user")
					.password(encoder.encode("password"))
					.email("user@mail.com")
					.firstName("username")
					.middleName("usermidname")
					.lastName("userlastname")
					.secondLastName("userseclastname")
					.build();
			
			user.addAuthority(roleUser);
			userRepo.save(user);
			
			UserEntity memeber = UserEntity.builder()
					.username("member")
					.password(encoder.encode("password"))
					.email("memeber@mail.com")
					.firstName("memebername")
					.middleName("memebermidname")
					.lastName("memeberlastname")
					.secondLastName("memeberseclastname")
					.build();
			
			memeber.addAuthority(roleUser);
			memeber.addAuthority(roleMember);
			userRepo.save(memeber);
			
			UserEntity coach = UserEntity.builder()
					.username("coach")
					.password(encoder.encode("password"))
					.email("coach@mail.com")
					.firstName("coachname")
					.middleName("coachmidname")
					.lastName("coachlastname")
					.secondLastName("coachseclastname")
					.build();
			
			coach.addAuthority(roleUser);
			coach.addAuthority(roleCoach);
			userRepo.save(coach);
			
			UserEntity receptionist = UserEntity.builder()
					.username("receptionist")
					.password(encoder.encode("password"))
					.email("receptionist@mail.com")
					.firstName("receptionistname")
					.middleName("receptionistmidname")
					.lastName("receptionistlastname")
					.secondLastName("receptionistseclastname")
					.build();
			
			receptionist.addAuthority(roleUser);
			receptionist.addAuthority(roleReceptionist);
			userRepo.save(receptionist);
			
			UserEntity admin = UserEntity.builder()
					.username("admin")
					.password(encoder.encode("password"))
					.email("admin@mail.com")
					.firstName("adminname")
					.middleName("adminmidname")
					.lastName("adminlastname")
					.secondLastName("adminseclastname")
					.build();
			
			admin.addAuthority(roleUser);
			admin.addAuthority(roleMember);
			admin.addAuthority(roleCoach);
			admin.addAuthority(roleReceptionist);
			admin.addAuthority(roleAdmin);
			userRepo.save(admin);
					
		}
	}

}
