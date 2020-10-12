package online.pelago.p4p.shipitinerary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import online.pelago.p4p.shipitinerary.security.UserInfoOpaqueTokenIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
			.authorizeRequests()
			.antMatchers("/swagger-resources/**").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.antMatchers("/swagger*/**").permitAll()
			.antMatchers("/v3/api-docs").permitAll()
			.antMatchers("/actuator/**").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
	}

	@Bean
	OpaqueTokenIntrospector introspector() {
		return new UserInfoOpaqueTokenIntrospector();
	}
}
