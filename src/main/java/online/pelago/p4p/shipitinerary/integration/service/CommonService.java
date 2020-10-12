package online.pelago.p4p.shipitinerary.integration.service;

import java.util.List;
import java.util.Optional;

import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;

public interface CommonService extends ServiceClient {

	default String getAtSeaPortCode() { return "XXX";}

	Optional<ShipDTO> getShip(String uiShip);
	
	List<PortDTO> getAllPorts();
	
	Optional<PortDTO> getPort(String uiPort);
	
	List<ShipDTO> getAllShips();
	
	
}
