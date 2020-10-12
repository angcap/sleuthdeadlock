package online.pelago.p4p.shipitinerary.repository.interceptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimeline;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimelineHistory;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineHistoryRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Slf4j
public class ShipPortTimelineRepositoryInterceptor {

	@Autowired
	@Lazy
	private ShipPortTimelineHistoryRepository historyRepository;

	/**
	 * Intercepts the execution of method save on {@link ShipPortTimelineRepository}
	 */
	@Pointcut("execution(public * (online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository).save(..))")
	public void shipPortTimelineRepositorySave() {
		// no body required
	}

	@Pointcut("execution(public * online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository+.saveAll(..))")
	public void shipPortTimelineRepositorySaveAll() {
		// no body required
	}

	@Pointcut("shipPortTimelineRepositorySave() || shipPortTimelineRepositorySaveAll()")
	public void behaviourMonitor() {
		// no body required
	}

	/**
	 * Around advice that creates a row in ShipPortTimelineHistory
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("online.pelago.p4p.shipitinerary.repository.interceptor.ShipPortTimelineRepositoryInterceptor.behaviourMonitor()")
	public Object createShipPortTimelineHistoryRow(ProceedingJoinPoint pjp) throws Throwable {
		String className = pjp.getSignature().getDeclaringTypeName();
		String methodName = pjp.getSignature().getName();
		LocalDateTime now = LocalDateTime.now();
		try {
			log.info("Intercepted ShipPortTimeline SAVE/SAVEALL : {}.{}", className, methodName);
			Object obj = pjp.proceed();

			if (obj instanceof ShipPortTimeline) {
				ShipPortTimeline t = (ShipPortTimeline) obj;
				log.info("ShipPortTimeline SAVE completed : {}.{}", className, methodName);
				historyRepository.save(new ShipPortTimelineHistory(t, now));
			} else if (obj instanceof Iterable<?>) {
				List<ShipPortTimeline> tList = (List<ShipPortTimeline>) obj;
				log.info("ShipPortTimeline SAVE ALL completed : {}.{}", className, methodName);
				List<ShipPortTimelineHistory> thistoryList = new ArrayList<>();
				
				for (ShipPortTimeline t : tList) {
					thistoryList.add(new ShipPortTimelineHistory(t,now));

				}
				historyRepository.saveAll(thistoryList);
			}

			log.info("Cloning ShipPortTimeline rows in ShipPortTimelineHistory");

			return obj;
		} catch (Throwable e) {
			log.info("Exception {} thrown in method : {}.{}", e.getClass().getCanonicalName(), className, methodName);
			throw e;
		}
	}

}
