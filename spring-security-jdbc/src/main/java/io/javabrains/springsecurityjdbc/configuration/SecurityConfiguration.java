package io.javabrains.springsecurityjdbc.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration /*extends WebSecurityConfigurerAdapter*/ {
	
	/*@Autowired 
	private DataSource dataSource;*/	 
	
	@Bean
	public UserDetailsManager getUserDetailsManager(DataSource dataSource) throws Exception {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		/* These are the two default queries and need not be declared for default configuration */
		/* But if your tables are different than the default, then these two queries need to be declared */
		jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from users where username = ? ");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username,authority from authorities where username = ? ");
		/**/
		/* This is not needed since the Users and authorities would already be present in the database */
		/*UserDetails user0 = User.builder()
				                .username("apurva")
				                .password("test")
				                .roles("ADMIN").build();
		UserDetails user1 = User.builder()
				                .username("vishakha")
				                .password("test")
				                .roles("USER").build();*/
		/*jdbcUserDetailsManager.createUser(user0);		
		jdbcUserDetailsManager.createUser(user1);*/
		return jdbcUserDetailsManager;
	}
	
	/* Old Way */
	/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)*/
		/*
		 * .withDefaultSchema() .withUser("apurva").password("test").roles("USER")
		 * .and() .withUser("vishakha").password("test").roles("ADMIN")
		 *//*.usersByUsernameQuery("select username, password, enabled from users where username = ? ")
		 .authoritiesByUsernameQuery("select username,authority from authorities where username = ? ");
		
	}*/
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	/* @Override */
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/admin").hasRole("ADMIN")
				                                     .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
				                                     .requestMatchers("/").permitAll())
		    .formLogin(Customizer.withDefaults()).build();
	}	

}
