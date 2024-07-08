package io.javabrains.springsecurityjwt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.javabrains.springsecurityjwt.filter.JwtRequestFilter;
import io.javabrains.springsecurityjwt.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, 
			PasswordEncoder passwordEncoder, UserDetailsService myUserDetailsService) throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(myUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		
		return new ProviderManager(authenticationProvider);
		/*
		 * return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
		 * .userDetailsService(myUserDetailsService) .passwordEncoder(passwordEncoder)
		 * .and() .build();
		 */
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
		            .authorizeHttpRequests(authorize -> authorize.requestMatchers("/authenticate").permitAll()
		            		                            .anyRequest().authenticated())
		            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
		            .build();
		/* I think that since the authentication mechanism is not specified, jwtRequestFilter is the only thing that does authentication.
		 * By default, Spring Security includes a SecurityFilterChain which has Form Login as the default authentication mechanism.
		 * However, since you are specifying a custom SecurityFilterChain, you need to specify the login mechanism. */
		/* jwtRequestFilter is added before the UsernamePasswordAuthenticationFilter.  UsernamePasswordAuthenticationFilter is towards the 
		 * middle of the SecurityFilterChain.
		 * The authorization filter will check and find that the request should be authenticated.  If Jwt is valid and  Authentication is already 
		 * available, then it will pass through.  All other filters will also have the Authentication available to them.
		 * 
		 * If jwt is not valid, the authorization filter will check and find that request should be authenticated. But there is no method of 
		 * authentication.  Hence, authentication will fail and request will not go through. */
	}

}
