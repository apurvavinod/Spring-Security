package com.javabrains.springsecuritystudy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	/* Use @Controller and not @RestController, since a RestController binds the return value to the responseBody and does not produce
	 * the desired result */
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	/* In case of /login?logout, the login page with the logout message is rendered.  In cse of /login?error, the login page with the 
	 * error message is displayed  */
	
	/* Thymeleaf is a modern server-side Java template engine for both web and standalone environments. */
	
	
	@RequestMapping("/logoutSuccess")
	public String logoutSuccess() {
		return "logout";
	}
}
