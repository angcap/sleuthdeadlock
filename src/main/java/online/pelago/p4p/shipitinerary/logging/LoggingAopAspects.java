package online.pelago.p4p.shipitinerary.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class LoggingAopAspects {

	private static final Logger log = LoggerFactory.getLogger(LoggingAopAspects.class);

	/** Pointcut for execution of methods on {@link Service} annotation */
	@Pointcut("execution(public * (@org.springframework.stereotype.Service online.pelago.p4p.shipitinerary..*).*(..))")
	public void serviceAnnotation() {
		// no body required
	}
	
	@Pointcut("execution(public * (online.pelago.p4p.shipitinerary.integration.service..*).*(..))")
	public void servicePackage() {
		// no body required
	}

	/** Pointcut for execution of methods on {@link Repository} annotation */
	@Pointcut("execution(public * (@org.springframework.stereotype.Repository online.pelago.p4p.shipitinerary..*).*(..))")
	public void repositoryAnnotation() {
		// no body required
	}

	/** Pointcut for execution of methods on {@link JpaRepository} interfaces */
	@Pointcut("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))")
	public void jpaRepository() {
		// no body required
	}

	/** Pointcut for execution of methods on {@link RestController} annotation */
	@Pointcut("execution(public * (@org.springframework.web.bind.annotation.RestController online.pelago.p4p.shipitinerary.*).*(..))")
	public void restControllerAnnotation() {
		// no body required
	}

	@Pointcut("serviceAnnotation() || repositoryAnnotation() || jpaRepository() || servicePackage()")
	public void behaviourMonitor() {
		// no body required
	}

	@Around("online.pelago.p4p.shipitinerary.logging.LoggingAopAspects.behaviourMonitor()")
	public Object logStartAndEnd(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		String className = pjp.getSignature().getDeclaringTypeName();
		String methodName = pjp.getSignature().getName();
		log.info("Entering method : {}.{}", className, methodName);
		
		try {
			Object obj = pjp.proceed();
			log.info("Leaving method : {}.{}", className, methodName);
			return obj;
		} catch (Throwable e) {
			log.info("Exception {} thrown in method : {}.{}", e.getClass().getCanonicalName(), className, methodName);
			throw e;
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Method {}.{} execution lasted: {} ms", className, methodName,
						System.currentTimeMillis() - startTime);
			}
		}
	}

}
