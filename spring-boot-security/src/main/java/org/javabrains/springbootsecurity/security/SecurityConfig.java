package org.javabrains.springbootsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(authorize -> authorize
				//.antMatchers("/**").hasRole("ADMIN")
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/user").hasAnyRole("USER","ADMIN")
				.antMatchers("/").permitAll()
				.anyRequest().authenticated())
			.formLogin();
		return httpSecurity.build();
	}
	
	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		UserDetails userDetails0 = User.withDefaultPasswordEncoder().username("apurva").password("test").roles("USER").build();
		UserDetails userDetails1 = User.withDefaultPasswordEncoder().username("vishakha").password("test").roles("USER", "ADMIN").build();
		return new InMemoryUserDetailsManager(userDetails0, userDetails1);
	}
}
