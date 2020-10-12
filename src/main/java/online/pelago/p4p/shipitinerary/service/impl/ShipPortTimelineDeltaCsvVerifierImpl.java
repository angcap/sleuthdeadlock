package online.pelago.p4p.shipitinerary.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.opencsv.exceptions.CsvConstraintViolationException;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportDeltaDTO;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.repository.ItineraryRepository;
import online.pelago.p4p.shipitinerary.service.ShipPortTimelineCsvVerifier;

@Service
@Qualifier("delta")
public class ShipPortTimelineDeltaCsvVerifierImpl implements ShipPortTimelineCsvVerifier<ImportDeltaDTO> {

	private Map<String, ShipDTO> shipsMap;
	private Map<String, List<PortDTO>> portsMap;
	
	@Autowired
	ItineraryRepository itineraryRepository;
	
	public void setUp(Map<String, ShipDTO> shipsMap, Map<String, List<PortDTO>> portsMap) {
		this.shipsMap = shipsMap;
		this.portsMap = portsMap;
		
	}
	
	@Override
	public boolean verifyBean(ImportDeltaDTO impExp) throws CsvConstraintViolationException {
		this.verifyConfiguration();
				if( !portsMap.containsKey(impExp.getPortCd().trim())) {
					throw new CsvConstraintViolationException("PortCD not valid ");
				}
				
				if(!shipsMap.containsKey(impExp.getShipCd().trim())) {
					throw new CsvConstraintViolationException("ShipCD not valid ");
				}
				if(!itineraryRepository.getItineraryByRegionSubRegionArea(impExp.getItinCD(),
						impExp.getSubRegion(), impExp.getRegion(), impExp.getArea()).isPresent()) {
					throw new CsvConstraintViolationException("Itinerary not valid ");
				}
			return true;
	
		
	}

	private void verifyConfiguration() {
		if(this.shipsMap == null || this.portsMap == null )
			throw new RuntimeException("Invalid setup for this Verifier");
	

	}

}
