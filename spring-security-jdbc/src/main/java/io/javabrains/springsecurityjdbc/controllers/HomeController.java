package io.javabrains.springsecurityjdbc.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public String getHome() {
		return ("<h2>Welcome</h2>");
	}
	
	@GetMapping("/user")
	public String getUser() {
		return ("<h2>Welcome User</h2>");
	}
	
	@GetMapping("/admin")
	public String getAdmin() {
		return ("<h2>Welcome Admin</h2>");
	}

}
