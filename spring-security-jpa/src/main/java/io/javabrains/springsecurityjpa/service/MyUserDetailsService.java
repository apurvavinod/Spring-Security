package io.javabrains.springsecurityjpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.javabrains.springsecurityjpa.domain.MyUserDetails;
import io.javabrains.springsecurityjpa.model.User;
import io.javabrains.springsecurityjpa.repository.UserRepository;

@Service(value = "myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//return new MyUserDetails(username);
		Optional<User> optionalUser = userRepository.findByUserName(username);
		
		User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User Name: " + username + " not found"));
		
		return new MyUserDetails(user);
	}

}
