package io.javabrains.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.springsecurityjwt.exception.AppServiceException;
import io.javabrains.springsecurityjwt.model.AuthenticationRequest;
import io.javabrains.springsecurityjwt.model.AuthenticationResponse;
import io.javabrains.springsecurityjwt.service.JwtUtilService;

@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtilService jwtUtilService;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World!";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		}
		catch (Exception e) {
			throw new AppServiceException("User could not be authenticated");
		}
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		final String jwtToken = jwtUtilService.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
	}
}
