package online.pelago.p4p.shipitinerary.integration.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 
 * A concrete implementation of a {@link AbstractCommonServiceImpl} that
 * simply provide {@link #getAddress()} using the autowired
 * {@code services.organisation.url} environment property with a default
 * fallback value.
 *
 */
@Service
@Profile("!openshift")
public class CommonServiceImplWithURI extends AbstractCommonServiceImpl {
	
	@Value("${services.common.url:http://localhost:8085}")
	private String serviceBaseUrl;

	@Override
	public String getAddress() {
		return serviceBaseUrl;
	}
}
