package online.pelago.p4p.shipitinerary.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

interface AuthenticationAwareController {
	
	default String getRemoteUser() {
		return getUserPrincipal().getName();
	}
	
	default boolean isUserInRole(String role) {
		return getAuthentication().getAuthorities().stream().anyMatch(autority -> autority.getAuthority().equals(role));
	}
	
	
	
	default OAuth2AuthenticatedPrincipal getUserPrincipal() {
		return (OAuth2AuthenticatedPrincipal) getAuthentication().getPrincipal();

	}
	
	default Authentication getAuthentication() {
		return  SecurityContextHolder.getContext()
				.getAuthentication();

	}
}
