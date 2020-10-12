package online.pelago.p4p.shipitinerary.integration.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;
import online.pelago.p4p.shipitinerary.integration.service.ServiceClient;
import online.pelago.p4p.shipitinerary.security.SecurityUtil;



/**
 * 
 * This abstract class provide the implementation of business operations defined
 * in {@link CommonService}, while delegating to concrete implementation
 * the service client behavior inherited from {@link ServiceClient}
 *
 */
@Slf4j
public abstract class AbstractCommonServiceImpl implements CommonService {
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("rawtypes")
	@Autowired
    private CircuitBreakerFactory cbFactory;

    public static final String COMMON_SERVICE_NAME = "common";
    
    public static final String COMMON_CONTEXT_PATH = "common";

    @Override
    public String getServiceName() {
        return COMMON_SERVICE_NAME;
    }
    
    @Override
    public String getContextPath() {
        return COMMON_CONTEXT_PATH;
    }
    
    @Override
    @Cacheable("ships")
    public Optional<ShipDTO> getShip(String uiShip) {
    	String serviceUri = String.format("%s/%s/ship/{ui}",
        		getAddress(), getContextPath()
        );
    	final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
    	return cbFactory.create("slow")
				.run(
						() -> {
							SecurityUtil.setSecurityContext(auth);							
							return Optional.of(restTemplate.getForEntity(serviceUri, ShipDTO.class, uiShip).getBody());
						},
				throwable -> {
					
					log.error("Circuit breaker triggered by " + (throwable.getMessage() != null ?throwable.getMessage():throwable.toString()) + " ...returning empty object",throwable);
					return Optional.empty();
				});
    }
    
    
    @Override
    @Cacheable("ships")
    public List<ShipDTO> getAllShips() {

        String serviceUri = String.format("%s/%s/ship",
        		getAddress(), getContextPath()
        );

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
		return Arrays.stream(cbFactory.create("slow")
                .run(
                        () -> {
                        	SecurityUtil.setSecurityContext(auth);
                        	return restTemplate.getForEntity(serviceUri, ShipDTO[].class).getBody();
                        },
                        throwable -> {
                        	log.error("Circuit breaker triggered by " + (throwable.getMessage() != null ?throwable.getMessage():throwable.toString()) + " ...returning empty list",throwable);
                                return new ShipDTO[0];
                        }
                )).collect(Collectors.toList());
    }
    
    

    @Override
    @Cacheable("ports")
    public List<PortDTO> getAllPorts() {

        String serviceUri = String.format("%s/%s/port",
        		getAddress(), getContextPath()
        );

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
		return Arrays.stream(cbFactory.create("slow")
                .run(
                        () -> {
                        	SecurityUtil.setSecurityContext(auth);
                        	return restTemplate.getForEntity(serviceUri, PortDTO[].class).getBody();
                        },
                        throwable -> {
                        	log.error("Circuit breaker triggered by " + (throwable.getMessage() != null ?throwable.getMessage():throwable.toString()) + " ...returning empty list",throwable);
                                return new PortDTO[0];
                        }
                )).collect(Collectors.toList());
    }

    @Override
    public Optional<PortDTO> getPort(String uiPort) {
    	String serviceUri = String.format("%s/%s/port/{uiPort}",
        		getAddress(), getContextPath()
        );
    	final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
    	return cbFactory.create("slow")
				.run(
						() -> {
							SecurityUtil.setSecurityContext(auth);							
							return Optional.of(restTemplate.getForEntity(serviceUri, PortDTO.class, uiPort).getBody());
						},
				throwable -> {
					
					log.error("Circuit breaker triggered by " + (throwable.getMessage() != null ?throwable.getMessage():throwable.toString()) + " ...returning empty object",throwable);
					return Optional.empty();
				});
    }

}
