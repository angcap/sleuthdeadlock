package online.pelago.p4p.shipitinerary.healthcheck.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoolStatusServiceImpl implements PoolStatusService{

	@Autowired
	private EntityManager em;
	
	/**
	 * Execute the native query "select SYSDATETIME()" against the configured database.  
	 */
	@Override
	public void checkStatus() {
		em.createNativeQuery("select SYSDATETIME()").getSingleResult();
	}
}
