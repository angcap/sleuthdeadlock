package online.pelago.p4p.shipitinerary.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import online.pelago.p4p.shipitinerary.entity.ShipPortTimelineHistory;

public interface ShipPortTimelineHistoryRepository extends JpaRepository<ShipPortTimelineHistory, Integer> {
	
	
	@Query("select h from ShipPortTimelineHistory as h where h.uiShip = :uiShip and ((:fromDate IS NULL AND :toDate is NULL) OR (h.utcUpdate between :fromDate and :toDate)) and (:cruiseCode is NULL or h.cruiseCode = :cruiseCode)")
	public List<ShipPortTimelineHistory> getHistoryByShipAndCriteria(@Param("uiShip") String uiShip,
			@Param("cruiseCode") String cruiseCode, @Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate);
	
	
	
	
}
