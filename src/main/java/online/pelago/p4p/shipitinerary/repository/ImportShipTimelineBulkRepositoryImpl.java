package online.pelago.p4p.shipitinerary.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.entity.OperativeStatus;
import online.pelago.p4p.shipitinerary.repository.impl.AbstractBulkRepository;

@Repository
public class ImportShipTimelineBulkRepositoryImpl extends AbstractBulkRepository implements ImportShipTimelineBulkRepository {

	@Override
	public String getBatchUpdateDml() {
		return "insert into ShipItinerary.ImportShipTimeline (shipCode,date,itineryCode,cruiseCode,portCode,portNameCountry,position,eta,etd,area,region,subRegion,operativeStatus,season,utcProcessed,utcDate) "
				+ " values (:shipCode,:date,:itineryCode,:cruiseCode,:portCode,:portNameCountry,:position,:eta,:etd,:area,:region,:subRegion,:operativeStatus,:season,:utcProcessed,:utcDate)";
	}

	@Override
	public Map<String, Object> getBatchUpdateParametersMap(ImportExportDTO itineray, String username,
			LocalDateTime utcUpdate) {
		Map<String, Object> m = new HashMap<>(10);
		m.put("shipCode", itineray.getShipCd());
		m.put("date", itineray.getDate());
		m.put("itineryCode", itineray.getItinCD());
		m.put("cruiseCode", itineray.getCruiseCode());
		m.put("portCode", itineray.getPortCd());
		m.put("portNameCountry", itineray.getPortNameCountry());
		m.put("position", itineray.getPosition());
		m.put("eta", itineray.getEta());
		m.put("etd", itineray.getEtd());
		m.put("area", itineray.getArea());
		m.put("region", itineray.getRegion());
		m.put("subRegion", itineray.getSubRegion());
		m.put("operativeStatus", OperativeStatus.OPERATIVE);
		m.put("season", itineray.getSeason());
		m.put("utcDate", utcUpdate);
		m.put("utcProcessed", utcUpdate);
		return m;	
	}

}
