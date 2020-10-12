package online.pelago.p4p.shipitinerary.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportDeltaDTO;
import online.pelago.p4p.shipitinerary.entity.OperativeStatus;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimeline;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;
import online.pelago.p4p.shipitinerary.mapper.ShipItineraryMapper;
import online.pelago.p4p.shipitinerary.repository.ImportShipTimelineRepository;
import online.pelago.p4p.shipitinerary.repository.ItineraryRepository;
import online.pelago.p4p.shipitinerary.repository.OperativeStatusRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository;
import online.pelago.p4p.shipitinerary.service.CSVParser;
import online.pelago.p4p.shipitinerary.service.ImportDeltaService;
import online.pelago.p4p.shipitinerary.service.ShipPortTimelineCsvVerifier;

@Service
public class ImportDeltaServiceImpl extends AbstractImportServiceImpl implements ImportDeltaService, CSVParser<ImportDeltaDTO> {


	@Autowired
	CommonService commonService;

	@Autowired
	ItineraryRepository itineraryRepository;

	@Autowired
	OperativeStatusRepository operativeStatusRepository;

	@Autowired
	ShipPortTimelineRepository shipPortTimelineRepository;
	
	@Autowired
	ImportShipTimelineRepository importShipTimelineRepository;

	@Autowired
	ShipItineraryMapper mapper;
	
	@Autowired
	@Qualifier("delta")
	@Lazy
	private ShipPortTimelineCsvVerifier<ImportDeltaDTO> csvBeanVerfier;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importDeltaCsv(byte[] file) throws BusinessRequirementViolationException, IOException, ElementNotFoundException {

		HeaderColumnNameMappingStrategy<ImportDeltaDTO> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
		mappingStrategy.setType(ImportDeltaDTO.class);

		this.csvBeanVerfier.setUp(getShipsMapByCode(), getPortsMapByIso3());
		
		List<ImportDeltaDTO> importData = this.parse(file, ImportDeltaDTO.class, mappingStrategy, ';');
		
		importShipTimelineRepository.saveAll(mapper.importDeltaDTOToImportEntityList(importData));
		
		List<OperativeStatus> opList = operativeStatusRepository.findByDescription("Operative");
		if (opList.isEmpty()) {
			throw new BusinessRequirementViolationException("Operative Status Operative non found.");
		}
		
		for (ImportDeltaDTO impExp : importData) {
			
			if(impExp.getStatus().equalsIgnoreCase("CANCELLED")) {
				Optional<ShipPortTimeline> optShipPortTimeline = shipPortTimelineRepository.findByShipAndDate(getShipsMapByCode().get(impExp.getShipCd()).getUiShip(), impExp.getDate());
				if(!optShipPortTimeline.isPresent()) {
					throw new ElementNotFoundException("ShipPortTimeline with ship "+impExp.getShipCd()+" and date "+impExp.getDate()+" non found.");
				}
				shipPortTimelineRepository.delete(optShipPortTimeline.get());
			}else if(impExp.getStatus().equalsIgnoreCase("NEW")) {
				
				impExp.setUiPort(this.getPort(impExp.getPortCd(), impExp.getPortNameCountry()).getUiPort());
				impExp.setUiShip(getShipsMapByCode().get(impExp.getShipCd()).getUiShip());
				impExp.setFkItinerary(this.getFkItinerary(impExp));
				impExp.setIsHomePort("0");
				impExp.setFkOperativeStatus(opList.get(0).getId());
				shipPortTimelineRepository.save(mapper.importDeltaDTOToEntity(impExp));
			}else if(impExp.getStatus().equalsIgnoreCase("UPDATED")) {
				Optional<ShipPortTimeline> optShipPortTimeline = shipPortTimelineRepository.findByShipAndDate(getShipsMapByCode().get(impExp.getShipCd()).getUiShip(), impExp.getDate());
				if(!optShipPortTimeline.isPresent()) {
					throw new ElementNotFoundException("ShipPortTimeline with ship "+impExp.getShipCd()+" and date "+impExp.getDate()+" non found.");
				}
				ShipPortTimeline timeline  = optShipPortTimeline.get();
				timeline.setCruiseCode(impExp.getCruiseCode());
				timeline.setEta(impExp.getEta());
				timeline.setEtd(impExp.getEtd());
				timeline.getItinerary().setId(this.getFkItinerary(impExp));
				
				shipPortTimelineRepository.save(timeline);
				
			}else {
				throw new BusinessRequirementViolationException("STATUS "+impExp.getStatus()+" is not valid.");
				
			}
		}
	}

	private Short getFkItinerary(ImportDeltaDTO impExp) throws ElementNotFoundException {
		Optional<Short> optItinerary = itineraryRepository.getItineraryByRegionSubRegionArea(impExp.getItinCD(),
				 impExp.getSubRegion(), impExp.getRegion(), impExp.getArea());
		if(!optItinerary.isPresent()) {
			throw new ElementNotFoundException("Itinerary with code "+impExp.getItinCD()+" and subRegion "+impExp.getSubRegion()+" and region "+impExp.getRegion()+" and area "+impExp.getArea()+" not found." );
		}
		return optItinerary.get();
	}
	
	@Override
	public BeanVerifier<ImportDeltaDTO> getBeanVerifier() {
		return csvBeanVerfier;
	}
	
}
