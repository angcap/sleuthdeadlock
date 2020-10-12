package online.pelago.p4p.shipitinerary.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.client.RestTemplate;

public class UserInfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	@Autowired
	private OAuthConfigBean oauthConfig;
	
	
	private OpaqueTokenIntrospector delegate;

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		return makeUserInfoRequest(this.getDelegate().introspect(token), token);
	}

	private OpaqueTokenIntrospector getDelegate() {
		if (this.delegate == null) {
			this.delegate = new NimbusOpaqueTokenIntrospector(oauthConfig.getIntrospectUri(), oauthConfig.getClientId(), oauthConfig.getClientSecret());
		}
		return this.delegate;
	}

	@SuppressWarnings("unchecked")
	private OAuth2AuthenticatedPrincipal makeUserInfoRequest(OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
			String token) {
		Map<String, Object> result = rest(token).getForObject(oauthConfig.getUserInfoUri(), Map.class);
		return new DefaultOAuth2AuthenticatedPrincipal(result.get("sub").toString(),
				oAuth2AuthenticatedPrincipal.getAttributes(),
				(Collection<GrantedAuthority>) oAuth2AuthenticatedPrincipal.getAuthorities());
	}

	RestTemplate rest(String token) {
		RestTemplate rest = new RestTemplate();
		rest.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().setBearerAuth(token);
			return execution.execute(request, body);
		});
		return rest;
	}


}
