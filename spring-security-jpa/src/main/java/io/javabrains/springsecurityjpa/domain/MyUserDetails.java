package io.javabrains.springsecurityjpa.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.javabrains.springsecurityjpa.model.User;

public class MyUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 7106138424844370605L;
	
	private String userName;
	
	private String password;
	private boolean active;
	private List<GrantedAuthority> listGrantedAuthority;
	
	public MyUserDetails(String userName) {
		this.userName = userName;
	}
	
	public MyUserDetails() {
		
	}
	
	public MyUserDetails(User user) {
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.active = user.isActive();
		this.listGrantedAuthority = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		return listGrantedAuthority;
	}

	@Override
	public String getPassword() {
		//return "pass";
		return password;
	}

	@Override
	public String getUsername() {
		return userName;		
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		//return true;
		return active;
	}

}
