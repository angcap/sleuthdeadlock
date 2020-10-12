package online.pelago.p4p.shipitinerary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import online.pelago.p4p.shipitinerary.entity.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Short> {

	@Query("select i.id from Itinerary i where i.code = :itineraryCode and i.season = :season and i.subRegion.id = "
			+ " ( select sr.id from SubRegion sr where sr.code = :subRegionCode and sr.region.id = "
			+ "  ( select r.id from Region r where r.code = :regionCode and r.area.id = (select a.id from Area a where a.code = :areaCode) )" + 
			")")
	public Optional<Short> getItineraryByRegionSubRegionAreaSeason(
			@Param("itineraryCode") String itineraryCode, 
			@Param("season") String  season, 
			@Param("subRegionCode") String subRegionCode, 
			@Param("regionCode") String regionCode, 
			@Param("areaCode") String areaCode);

	@Query("select i.id from Itinerary i where i.code = :itineraryCode and i.subRegion.id = "
			+ " ( select sr.id from SubRegion sr where sr.code = :subRegionCode and sr.region.id = "
			+ "  ( select r.id from Region r where r.code = :regionCode and r.area.id = (select a.id from Area a where a.code = :areaCode) )" + 
			")")
	public Optional<Short> getItineraryByRegionSubRegionArea(
			@Param("itineraryCode") String itineraryCode, 
			@Param("subRegionCode") String subRegionCode, 
			@Param("regionCode") String regionCode, 
			@Param("areaCode") String areaCode);
}
