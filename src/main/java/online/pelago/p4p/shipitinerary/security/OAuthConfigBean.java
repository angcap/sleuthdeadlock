package online.pelago.p4p.shipitinerary.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@lombok.Data
public class OAuthConfigBean {

	@Value("${OAUTH_SERVER:https://identity}/oauth2/introspect")
	private String introspectUri;

	@Value("${OAUTH_SERVER:https://identity}/oauth2/userinfo")
	private String userInfoUri;

	@Value("${OAUTH_ADMIN_USER:setme}")
	private String clientId;

	@Value("${OAUTH_ADMIN_PASSWORD:setme}")
	private String clientSecret;

}
