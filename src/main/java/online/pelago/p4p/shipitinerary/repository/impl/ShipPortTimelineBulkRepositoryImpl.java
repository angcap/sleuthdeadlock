package online.pelago.p4p.shipitinerary.repository.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineBulkRepository;

@Repository
public class ShipPortTimelineBulkRepositoryImpl extends AbstractBulkRepository implements ShipPortTimelineBulkRepository {

	@Override
	public
	String getBatchUpdateDml() {
		return "insert into ShipItinerary.ShipPortTimeline (uiShip,date,uiPort,eta,etd,position,cruiseCode,utcUpdate,fkUsername,fkItinerary,fkOperativeStatus,isHomePort) "
				+ " values (:uiShip,:date,:uiPort,:eta,:etd,:position,:cruiseCode,:utcUpdate,:fkUsername,:fkItinerary,:fkOperativeStatus,:isHomePort)";
	}

	@Override
	public
	Map<String, Object> getBatchUpdateParametersMap(ImportExportDTO itineray, String username, LocalDateTime utcUpdate) {
		Map<String, Object> m = new HashMap<>(10);
		m.put("uiShip", itineray.getUiShip());
		m.put("date", itineray.getDate());
		m.put("uiPort", itineray.getUiPort());
		m.put("eta", itineray.getEta());
		m.put("etd", itineray.getEtd());
		m.put("position", itineray.getPosition());
		m.put("cruiseCode", itineray.getCruiseCode());
		m.put("utcUpdate", utcUpdate);
		m.put("fkUsername", username);
		m.put("fkItinerary", itineray.getFkItinerary());
		m.put("fkOperativeStatus", itineray.getFkOperativeStatus());
		m.put("isHomePort", itineray.getIsHomePort());
		return m;
	}

}
