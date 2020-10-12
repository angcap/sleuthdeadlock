package online.pelago.p4p.shipitinerary.integration.service;

/**
 * 
 * A base interface for a microservice client
 *
 */
public interface ServiceClient {
	
	/**
	 * 
	 * @return the service url (for example http://employee:8080 or http://localhost:8088)
	 */
	String getAddress();

	/**
	 * 
	 * @return the cluster service name (for example employee) 
	 */
	String getServiceName();
	
	/**
	 * 
	 * @return the application context path (for example employees)
	 */
	String getContextPath();
}
