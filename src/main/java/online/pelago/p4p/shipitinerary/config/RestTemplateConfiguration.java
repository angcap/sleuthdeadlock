package online.pelago.p4p.shipitinerary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestTemplateConfiguration {

	@Bean
	RestTemplate rest() {
		RestTemplate rest = new RestTemplate();
		rest.getInterceptors().add((request, body, execution) -> {
			log.trace("Intercepted rest template call {}", request.getURI());
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			log.trace("authentication {}", authentication);
			if (authentication == null) {
				return execution.execute(request, body);
			}

			if (!(authentication.getCredentials() instanceof AbstractOAuth2Token)) {
				return execution.execute(request, body);
			}

			AbstractOAuth2Token token = (AbstractOAuth2Token) authentication.getCredentials();
			log.trace("Adding bearer header with token {}", token.getTokenValue());
			request.getHeaders().setBearerAuth(token.getTokenValue());
			return execution.execute(request, body);
		});
		return rest;
	}
}
