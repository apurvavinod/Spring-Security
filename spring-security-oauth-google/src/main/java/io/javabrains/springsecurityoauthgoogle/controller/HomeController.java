package io.javabrains.springsecurityoauthgoogle.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	@GetMapping("/")
	public String getHome(HttpServletRequest request, Authentication authentication1) {
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			Authentication authentication = (Authentication) principal;
			System.out.println("Principal Name: " + principal.getName());
			System.out.println("Authentication Name: " + authentication.getName());
			
			System.out.println("Authentication 1 Name: " + authentication1.getName());
			/*
			 * UserDetails userDetails = (UserDetails) principal;
			 * System.out.println("User Details Name: " + userDetails.getUsername());
			 */
			
			OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("google", principal.getName());
			
			if (authorizedClient != null) {
				System.out.println("OAuth Client Principal: " + authorizedClient.getPrincipalName());	
				
				String userInfoEndpointUri = authorizedClient.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
				if (StringUtils.isNotBlank(userInfoEndpointUri)) {
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue());
					HttpEntity<Object> requestEntity = new HttpEntity("", httpHeaders);
					
					ResponseEntity<Map> responseEntity = 
							restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, requestEntity, Map.class);
					Map userAttributes = responseEntity.getBody();
					System.out.println("Google User Name: " + (String) userAttributes.get("name"));
					userAttributes.entrySet().forEach(es -> {
						System.out.println(es.toString());
					});
				}
			}			
		}
		
		
		
		return "index.html";
	}
}
