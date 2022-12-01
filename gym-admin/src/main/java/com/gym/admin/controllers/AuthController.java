package com.gym.admin.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.admin.data.entities.UserEntity;
import com.gym.admin.data.payloads.MessageResponse;
import com.gym.admin.data.payloads.security.IsAuthenticatedResponse;
import com.gym.admin.data.payloads.security.LoginRequest;
import com.gym.admin.data.payloads.security.LoginResponse;
import com.gym.admin.data.payloads.security.SignupRequest;
import com.gym.admin.security.JwtUtils;
import com.gym.admin.security.UserDetailsImpl;
import com.gym.admin.services.UserService;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
	
	private static final Logger log = 
			LoggerFactory.getLogger(AuthController.class);
	
	// recommended value 1800
	private static final int COOKIE_MAX_AGE = 1800;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping(path = "/login")
	public ResponseEntity<LoginResponse> authenticateUser(
			@Valid @RequestBody LoginRequest loginRequest,
			HttpServletResponse response) throws AuthenticationException {
		
		System.out.println("username: " + loginRequest.getUsername() + " password: " + loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext()
			.setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		Cookie cookie = JwtUtils.createJwtCookie(COOKIE_MAX_AGE, jwt);
		response.addCookie(cookie);
		
		UserDetailsImpl userDetails = 
				(UserDetailsImpl) authentication.getPrincipal();
		
		List<String> roles = userDetails.getAuthorities()
				.stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		log.info("User " + userDetails.getUsername() + " logged in successfully");
		
		return ResponseEntity.ok(
				new LoginResponse(
						userDetails.getId(),
						userDetails.getUsername(),
						userDetails.getEmail(),
						roles));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(
			@Valid @RequestBody SignupRequest signupRequest) {
		
		if(userService.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if(userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		
		UserEntity user = userService.registerNewUser(signupRequest);
		
		log.info("New User, with username " + user.getUsername() + ", registered successfully");
		
		return ResponseEntity.ok(
				new MessageResponse("User registered successfully!"));
		
	}
	
	@GetMapping(path = "/logout")
	public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
		
		UserDetailsImpl principal = null;

		/*
		 *  When session/token expired, logout is called but Principal object is not
		 *  longer a UserDetailsImpl, anonymousUser is placed instead in the Authentication
		 *  object. In such case. logging is not perform here. It can be done in the
		 *  Authentication JWT Filter.
		 */
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass() == UserDetailsImpl.class) {
			principal = 
					(UserDetailsImpl) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
		}
		
		//Actual log out process
		Cookie cookie = JwtUtils.createJwtCookie(0, null);
		response.addCookie(cookie);
		SecurityContextHolder.getContext().setAuthentication(null);
		
		if(principal != null)
			log.info("User " + principal.getUsername() + " logged out successfully");
		
		return ResponseEntity
				.ok(new MessageResponse("User logged out successfully!"));
	}
	
	@GetMapping(path="/isAuthenticated")
	public ResponseEntity<?> isLoggedin(HttpServletRequest request) {
		
		boolean response = false;
		
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			
			Cookie tokenCookie = null;
			for(Cookie cookie : cookies) {
				if(cookie.getName().equalsIgnoreCase("jwt-token"))
					tokenCookie = cookie;
			}
			
			if(tokenCookie != null) {
				String jwt = tokenCookie.getValue();
				if(jwt != null && jwtUtils.validateJwtToken(jwt))
					response = true;
			}
		}
		
		return ResponseEntity
				.ok(new IsAuthenticatedResponse(response));
	}


}
