package online.pelago.p4p.shipitinerary.integration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import online.pelago.p4p.shipitinerary.integration.service.DiscoverableServiceClient;



/**
 * 
 * A concrete implementation of a {@link DiscoverableServiceClient} that simply
 * provide {@link #getDiscoveryClient()} using the autowired
 * {@code DiscoveryClient}.
 *
 */
@Service
@Profile("openshift")
public class CommonServiceImplWithDiscoveryClient extends AbstractCommonServiceImpl
		implements DiscoverableServiceClient {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Override
	public DiscoveryClient getDiscoveryClient() {
		return discoveryClient;
	}
}
