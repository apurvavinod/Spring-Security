package io.javabrains.springsecurityjwt.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.javabrains.springsecurityjwt.service.JwtUtilService;
import io.javabrains.springsecurityjwt.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtilService jwtUtilService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeaderValue = request.getHeader("Authorization");		
		if (authHeaderValue != null && authHeaderValue.startsWith("Bearer ")) {
			String jwtToken = authHeaderValue.substring(7);
			
			String userName = jwtUtilService.extractUserName(jwtToken);
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
				if (jwtUtilService.validateToken(jwtToken, userDetails)) {
					
					/* Spring sets the Authentication by Default.  But we will set the Authentication only if Token is validated */
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					/******/
				}
			}			
		}
		filterChain.doFilter(request, response);
	}

}
