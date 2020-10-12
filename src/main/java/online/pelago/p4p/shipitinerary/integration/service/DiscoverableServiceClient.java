package online.pelago.p4p.shipitinerary.integration.service;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import online.pelago.p4p.shipitinerary.exceptions.DiscoveryClientException;




/**
 * 
 * Provides a default implementation for {@link ServiceClient#getAddress()} using {@link #getDiscoveryClient()},
 * the {@link #getDiscoveryClient()} must be managed by concrete implementation. 
 *
 */
public interface DiscoverableServiceClient extends ServiceClient {
	
    @Override
    default String getAddress() {
        List<ServiceInstance> instances =
                getDiscoveryClient().getInstances(getServiceName());
        if (instances.isEmpty()) {
            throw new DiscoveryClientException(getServiceName() + " service is not present");
        }
        return instances.get(0).getUri().toString();
    }

    DiscoveryClient getDiscoveryClient();
}
