package online.pelago.p4p.shipitinerary.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing
@Profile("!test")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
				  .map(SecurityContext::getAuthentication)
				  .filter(Authentication::isAuthenticated)
				  .map(Authentication::getPrincipal)
				  .map(p -> p instanceof UserDetails ? ((UserDetails) p).getUsername() :((AuthenticatedPrincipal) p).getName());
	  }
}
