package online.pelago.p4p.shipitinerary.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.entity.OperativeStatus;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;
import online.pelago.p4p.shipitinerary.mapper.ShipItineraryMapper;
import online.pelago.p4p.shipitinerary.repository.ImportShipTimelineBulkRepository;
import online.pelago.p4p.shipitinerary.repository.ImportShipTimelineRepository;
import online.pelago.p4p.shipitinerary.repository.ItineraryRepository;
import online.pelago.p4p.shipitinerary.repository.OperativeStatusRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineBulkRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository;
import online.pelago.p4p.shipitinerary.service.CSVParser;
import online.pelago.p4p.shipitinerary.service.ImportExportService;
import online.pelago.p4p.shipitinerary.service.ShipPortTimelineCsvVerifier;

@Service
public class ImportExportServiceImpl extends AbstractImportServiceImpl implements ImportExportService, CSVParser<ImportExportDTO> {


	private static final char SEPARATOR = ';'; 

	@Autowired @Lazy
	private CommonService commonService;

	@Autowired @Lazy
	private ItineraryRepository itineraryRepository;

	@Autowired @Lazy
	private OperativeStatusRepository operativeStatusRepository;

	@Autowired @Lazy
	private ShipPortTimelineRepository shipPortTimelineRepository;
	
	@Autowired @Lazy
	private ShipPortTimelineBulkRepository shipPortTimelineBulkRepository;
	
	@Autowired @Lazy
	private ImportShipTimelineBulkRepository importShipTimelineBulkRepository;

	@Autowired @Lazy
	private ImportShipTimelineRepository importShipTimelineRepository;
	
	@Autowired @Lazy
	private ShipItineraryMapper mapper;

	@Autowired
	@Lazy
	private ShipPortTimelineCsvVerifier<ImportExportDTO> csvBeanVerfier;
	
	

	// ShipCd: it is the Ship Code
	// - Date: it is the date when the itinineray change will happens in the format
	// DD/MM/YY
	// - ItinCD: it is the Itinerary Code
	// - Season: the season of the cruise
	// - CruiseNbr: it is the Cruise number
	// - PortCd: it is the Port Code
	// - PortName,Country: it is the code made by Port and Country
	// - Position: it is the position of the ship in the port (Alongside, Anchorage,
	// Along/Anch, At Sea)
	// - ETA: it is the estimate time of arrival in Port local hours in the format
	// HH:MM
	// - ETD: it is the estimate time of departure in Port local hours in the format
	// HH:MM
	// - Area: Geographic position
	// - Region: Geographic position
	// - Subregion: Geographic position

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void importCsv(byte[] file, String username) throws BusinessRequirementViolationException, IOException, ElementNotFoundException {

		HeaderColumnNameMappingStrategy<ImportExportDTO> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
		mappingStrategy.setType(ImportExportDTO.class);
		
		this.csvBeanVerfier.setUp(getShipsMapByCode(), getPortsMapByIso3());
		
		List<ImportExportDTO> importData = this.parse(file, ImportExportDTO.class, mappingStrategy, SEPARATOR);
		
		List<OperativeStatus> opList = operativeStatusRepository.findByDescription(OperativeStatus.OPERATIVE);
		if (opList.isEmpty()) {
			throw new BusinessRequirementViolationException("Operative Status Operative non found.");
		}
		
		importShipTimelineBulkRepository.save(importData, username);

		for (ImportExportDTO impExp : importData) {			
			impExp.setIsHomePort("0");
			impExp.setFkOperativeStatus(opList.get(0).getId());
		}
		List<String> uiShipList = importData
				.stream()
				.map(impExp -> getShipsMapByCode().get(impExp.getShipCd()).getUiShip())
				.distinct().collect(Collectors.toList());
		if (!importData.isEmpty()) {
			shipPortTimelineRepository.deleteByUiShipIn(uiShipList);
			shipPortTimelineBulkRepository.save(importData, username);
		}
	}

	@Override
	public void exportCsv(PrintWriter writer)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		HeaderColumnNameMappingStrategy<ImportExportDTO> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
		mappingStrategy.setType(ImportExportDTO.class);
		mappingStrategy.setColumnOrderOnWrite(new OrderedComparatorIgnoringCase(ImportExportDTO.FIELDS_ORDER));
		this.format(writer, this.exportShipPortTimelines(), mappingStrategy, SEPARATOR);
	}

	@Override
	public List<ImportExportDTO> exportShipPortTimelines()
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		
		Map<String,PortDTO> portsMap = commonService.getAllPorts().stream().collect(Collectors.toMap(x -> x.getUiPort(), x -> x));

		checkMaps(portsMap, getShipsMapByCode());

		return this.shipPortTimelineRepository.findAll().stream()
				.map(target -> ImportExportDTO.builder()
						.shipCd(getShipsMapByUid().get(target.getUiShip()).getCode())
						.date(target.getDate())
						.itinCD(target.getItinerary().getCode())
						.season(target.getItinerary().getSeason())
						.cruiseCode(target.getCruiseCode())
						.portCd(portsMap.get(target.getUiPort()).getIso3())
						.portNameCountry(portsMap.get(target.getUiPort()).getDescription() + ","
								+ portsMap.get(target.getUiPort()).getCountry().getDescription())
						.position(target.getPosition())
						.eta(target.getEta())
						.etd(target.getEtd())
						.area(target.getItinerary().getSubRegion().getRegion().getArea().getCode())
						.region(target.getItinerary().getSubRegion().getRegion().getCode())
						.subRegion(target.getItinerary().getSubRegion().getCode())
						.build())				
				.sorted((a,b)-> {
					if (a.getShipCd().compareTo(b.getShipCd()) == 0) {
						return a.getDate().compareTo(b.getDate());
					}
					return a.getShipCd().compareTo(b.getShipCd());
				})
				.collect(Collectors.toList());

	}

	private void checkMaps(Map<String, PortDTO> portsMap, Map<String, ShipDTO> shipsMap)
			throws ElementNotFoundException {

		if (portsMap.isEmpty()) {
			throw new ElementNotFoundException("Unable to complete the operation with empty ports list");
		}
		if (shipsMap.isEmpty()) {
			throw new ElementNotFoundException("Unable to complete the operation with empty ships list");
		}

	}

	@Override
	public BeanVerifier<ImportExportDTO> getBeanVerifier() {
		return csvBeanVerfier;
	}

}
