package online.pelago.p4p.shipitinerary.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.opencsv.exceptions.CsvConstraintViolationException;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.repository.ItineraryRepository;
import online.pelago.p4p.shipitinerary.service.ShipPortTimelineCsvVerifier;

@Service
public class ShipPortTimelineFullCsvVerifierImpl extends AbstractImportServiceImpl
		implements ShipPortTimelineCsvVerifier<ImportExportDTO> {

	@Autowired
	@Lazy
	private ItineraryRepository itineraryRepository;

	@Override
	public boolean verifyBean(ImportExportDTO impExp) throws CsvConstraintViolationException {
		
		if (!getPortsMapByIso3().containsKey(impExp.getPortCd().trim())) {
			throw new CsvConstraintViolationException("PortCD not valid ");
		}
		try {
			impExp.setUiPort(getPort(impExp.getPortCd(), impExp.getPortNameCountry()).getUiPort());
		} catch (ElementNotFoundException e) {
			throw new CsvConstraintViolationException("Unable to find port with PortCd: "+impExp.getPortCd()+" and PortNameCountry: "+impExp.getPortNameCountry());
		}
		
		if (!getShipsMapByCode().containsKey(impExp.getShipCd().trim())) {
			throw new CsvConstraintViolationException("ShipCD not valid ");
		}
		impExp.setUiShip(getShipsMapByCode().get(impExp.getShipCd().trim()).getUiShip());

		Optional<Short> itinerary = itineraryRepository.getItineraryByRegionSubRegionAreaSeason(impExp.getItinCD(), impExp.getSeason(),
				impExp.getSubRegion(), impExp.getRegion(), impExp.getArea());
		if (!itinerary.isPresent()) {
			throw new CsvConstraintViolationException("Itinerary not valid ");
		}
		impExp.setFkItinerary(itinerary.get());
		
		return true;
	}

	@Override
	public void setUp(Map<String, ShipDTO> shipMap, Map<String, List<PortDTO>> portMap) {
		this.setPortsMap(portMap);
		this.setShipsMapByCode(shipMap);
	}

}
