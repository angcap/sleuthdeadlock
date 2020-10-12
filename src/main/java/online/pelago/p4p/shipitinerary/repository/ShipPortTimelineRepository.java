package online.pelago.p4p.shipitinerary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import online.pelago.p4p.shipitinerary.dto.ShipItineraryDTO;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimeline;

public interface ShipPortTimelineRepository extends JpaRepository<ShipPortTimeline, Integer> {

	
	@Override
	<S extends ShipPortTimeline> List<S> saveAll(Iterable<S> entities);


	@Query("SELECT "
			+ " min(spt.date) as startDate, "
			+ " max(spt.date) as endDate, "
			+ " spt.itinerary.code as itineraryCode,"
			+ " spt.itinerary.subRegion.region.area.code as areaCode, "
			+ " spt.itinerary.subRegion.region.area.description as areaDescription, "
			+ " spt.itinerary.subRegion.region.code as regionCode, "
			+ " spt.itinerary.subRegion.region.description as regionDescription,"
			+ " spt.itinerary.subRegion.code as subRegionCode, "
			+ " spt.itinerary.subRegion.description as subRegionDescription, "
			+ " spt.itinerary.subRegion.color as color"
			+ " FROM ShipPortTimeline spt WHERE spt.uiShip = :uiShip "
			+ " group by "
			+ " spt.itinerary.code,spt.itinerary.subRegion.region.area.description,spt.itinerary.subRegion.region.area.code, spt.itinerary.subRegion.region.code, spt.itinerary.subRegion.region.description, spt.itinerary.subRegion.code,spt.itinerary.subRegion.description,spt.itinerary.subRegion.color")
	List<ShipItineraryDTO> getShipPortTimelineByUiShip(@Param("uiShip") String uiShip);

	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "operativeStatus", "itinerary" })
	@Query("select spt from ShipPortTimeline spt where spt.uiShip = :uiShip and ((:fromDate IS NULL AND :toDate is NULL) OR (spt.date between :fromDate and :toDate))")
	List<ShipPortTimeline> getShipPortTimelines(@Param("uiShip") String uiShip, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	Optional<ShipPortTimeline> findByUiShipPortTimeline(String uiShipPortTimeline);

	@EntityGraph(type = EntityGraphType.LOAD, attributePaths = { "operativeStatus", "itinerary" })
	List<ShipPortTimeline> findByUiShipInAndDateBetween(List<String> uiShipList, LocalDate fromDate, LocalDate toDate);

	List<ShipPortTimeline> findByDateBetween(LocalDate fromDate, LocalDate toDate);

	@Modifying
	@Query("delete from ShipPortTimeline spt where spt.uiShip in (:uiShipList) ")
	Integer deleteByUiShipIn(@Param("uiShipList") List<String> uiShipList);
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void delete(ShipPortTimeline shipPortTimeline);
	
	@Query("select spt from ShipPortTimeline spt where spt.uiShip = :uiShip and spt.date = :date")
	Optional<ShipPortTimeline> findByShipAndDate(@Param("uiShip") String uiShip, @Param("date") LocalDate date);
   
}
