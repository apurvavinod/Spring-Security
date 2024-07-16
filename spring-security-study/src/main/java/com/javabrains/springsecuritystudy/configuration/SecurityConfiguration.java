package com.javabrains.springsecuritystudy.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
	public InMemoryUserDetailsManager getUserDetails() {
		UserDetails userDetails0 = User.withDefaultPasswordEncoder()
				                      .username("apurva")
				                      .password("vinod")
				                      .roles("USER", "ADMIN")
				                      .build();
		UserDetails userDetails1 = User.withDefaultPasswordEncoder()
				                       .username("amey")
				                       .password("tambe")
				                       .roles("USER")
				                       .build();
		
		return new InMemoryUserDetailsManager(userDetails0, userDetails1);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers("/admin").hasRole("ADMIN")
			         .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
			         .requestMatchers("/").hasAnyRole("USER", "ADMIN")
					/* .requestMatchers("/").permitAll() */
			         .anyRequest().authenticated();
		}).formLogin(form -> form.loginPage("/login").permitAll())
		.csrf(csrf -> csrf.disable())	/* If you disable csrf, even GET logout is permitted, else, you need a POST logout request */
		.logout(logout -> logout.logoutSuccessUrl("/logoutSuccess").permitAll());
		
		return httpSecurity.build();
	}
}