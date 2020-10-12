package online.pelago.p4p.shipitinerary.service;

import java.util.List;
import java.util.Map;

import com.opencsv.bean.BeanVerifier;

import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;

public interface ShipPortTimelineCsvVerifier<T> extends BeanVerifier<T> {

	/**
	 * Set up data maps used for data validation, be sure to call this method before
	 * pass the <code>BeanVerifier</code> instance to <code>CsvToBeanBuilder.withVerifier</code>
	 * 
	 * @param shipMap
	 * @param portMap
	 */
	void setUp(Map<String, ShipDTO> shipMap, Map<String, List<PortDTO>> portMap);
}
