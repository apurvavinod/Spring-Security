package io.javabrains.springsecurityjpa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeHttpRequests(authorize -> authorize.requestMatchers("/admin").hasRole("ADMIN")
				                                                 .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
				                                                 .requestMatchers("/").permitAll())
		            .formLogin(Customizer.withDefaults())
		            .build();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
