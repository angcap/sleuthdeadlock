package online.pelago.p4p.shipitinerary.service;

import java.time.LocalDate;
import java.util.List;

import online.pelago.p4p.shipitinerary.dto.ShipItineraryDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineInputDTO;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;

public interface ShipItineraryService {

	public List<ShipItineraryDTO> getShipItineraries(String uiShip);
	
	public List<ShipPortTimelineDTO> getShipPortTimelines(String uiShip, LocalDate fromDate, LocalDate toDate) throws ElementNotFoundException;

	public ShipPortTimelineDTO getShipPortTimeline(String ui) throws ElementNotFoundException;

	public List<ShipPortTimelineDTO> getShipItineraries(List<String> uiShipList, LocalDate fromDate, LocalDate toDate);
	
	public void updateShipPortTimeline(ShipPortTimelineInputDTO shipPortTimelineInputDTO) throws ElementNotFoundException, BusinessRequirementViolationException;
	
	public List<ShipPortTimelineDTO> getHistory(String uiShip, String cruiseCode, LocalDate fromDate, LocalDate toDate);
	
	
}
