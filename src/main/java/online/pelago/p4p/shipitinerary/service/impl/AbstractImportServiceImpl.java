package online.pelago.p4p.shipitinerary.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;


import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;

public abstract class AbstractImportServiceImpl {

	private Map<String, List<PortDTO>> portsMap;
	private Map<String, ShipDTO> shipsMapByCode;
	
	@Autowired
	private CommonService commonService;
	
	protected void setPortsMap(Map<String, List<PortDTO>> portsMap) {
		this.portsMap = portsMap;
	}


	protected void setShipsMapByCode(Map<String, ShipDTO> shipsMapByCode) {
		this.shipsMapByCode = shipsMapByCode;
	}

	public Map<String, List<PortDTO>> getPortsMapByIso3() {
		if(this.portsMap == null) {
			this.portsMap = this.commonService.getAllPorts()
					.stream()
					.collect(Collectors.toMap(
						PortDTO::getIso3, 
						Arrays::asList,
						(a,b)->{
							List<PortDTO> l = new ArrayList<>();
							l.addAll(a);
							l.addAll(b);
							return l;
						}
					));
		}
		return portsMap;
	}


	public Map<String, ShipDTO> getShipsMapByCode() {
		if(this.shipsMapByCode == null) {
			this.shipsMapByCode = this.commonService.getAllShips()
					.stream()
					.collect(Collectors.toMap(
							ShipDTO::getCode, 
							x -> x));
		}
		return shipsMapByCode;
	}
	
	public Map<String, ShipDTO> getShipsMapByUid() {
		return this.getShipsMapByCode().values().stream().collect(Collectors.toMap(ShipDTO::getUiShip, x->x));
	}
	
	public PortDTO getPort(@NotNull String portCd,@NotNull String portNameCountry) throws ElementNotFoundException {
		Optional<PortDTO> p = getPortsMapByIso3().get(portCd)
		.stream()
		.filter(
				prt -> portNameCountry.toLowerCase().indexOf(prt.getCountry().getDescription().toLowerCase()) > -1)
		.findFirst();
		if (!p.isPresent()) {
			throw new ElementNotFoundException("Port with code "+portCd+" and country"+portNameCountry+ " not found.");
		}
		return p.get();
	}

}
