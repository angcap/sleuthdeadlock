package online.pelago.p4p.shipitinerary.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import online.pelago.p4p.shipitinerary.dto.ShipItineraryDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineInputDTO;
import online.pelago.p4p.shipitinerary.entity.Area;
import online.pelago.p4p.shipitinerary.entity.OperativeStatus;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimeline;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimelineHistory;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;
import online.pelago.p4p.shipitinerary.mapper.ShipItineraryMapper;
import online.pelago.p4p.shipitinerary.repository.AreaRepository;
import online.pelago.p4p.shipitinerary.repository.OperativeStatusRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineHistoryRepository;
import online.pelago.p4p.shipitinerary.repository.ShipPortTimelineRepository;
import online.pelago.p4p.shipitinerary.service.ShipItineraryService;

@Service
public class ShipItineraryServiceImpl implements ShipItineraryService {

	@Autowired
	private ShipPortTimelineRepository shipPortTimelineRepository;

	@Autowired
	private ShipPortTimelineHistoryRepository historyRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private OperativeStatusRepository operativeStatusRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ShipItineraryMapper mapper;

	@Override
	@Transactional
	public List<ShipItineraryDTO> getShipItineraries(String uiShip) {

		return shipPortTimelineRepository.getShipPortTimelineByUiShip(uiShip);

	}

	@Override
	@Transactional(readOnly = true)
	public List<ShipPortTimelineDTO> getShipPortTimelines(String uiShip, LocalDate fromDate, LocalDate toDate)
			throws ElementNotFoundException {

		Optional<ShipDTO> shipOpt = commonService.getShip(uiShip);
		if (!shipOpt.isPresent()) {
			throw new ElementNotFoundException("The ship with uid: " + uiShip + " is not found.");
		}

		List<ShipPortTimelineDTO> shipPortTimelineList = mapper.shipPortTimelineListToDtoList(
				shipPortTimelineRepository.getShipPortTimelines(uiShip, fromDate, toDate));

		return addPortDetail(shipPortTimelineList);

	}

	@Override
	@Transactional
	public ShipPortTimelineDTO getShipPortTimeline(String ui) throws ElementNotFoundException {
		Optional<ShipPortTimeline> shipPortTimelineOpt = shipPortTimelineRepository.findByUiShipPortTimeline(ui);
		if (!shipPortTimelineOpt.isPresent()) {
			throw new ElementNotFoundException("The ShipPortTimeline with uid: " + ui + " is not found.");
		}
		ShipPortTimeline shipPortTimeline = shipPortTimelineOpt.get();
		ShipPortTimelineDTO shipPortTimelineDTO = mapper.shipPortTimelineToDTO(shipPortTimeline);
		List<PortDTO> ports = commonService.getAllPorts();
		Map<String, PortDTO> mappaResults = ports.stream().collect(Collectors.toMap(x -> x.getUiPort(), x -> x));
		if (shipPortTimeline.getUiPort() != null) {
			shipPortTimelineDTO.setPort(mappaResults.get(shipPortTimeline.getUiPort()));
		}
		return shipPortTimelineDTO;
	}

	@Override
	@Transactional
	public void updateShipPortTimeline(ShipPortTimelineInputDTO shipPortTimelineInputDTO)
			throws ElementNotFoundException, BusinessRequirementViolationException {

		if (StringUtils.isNotEmpty(shipPortTimelineInputDTO.getUiShipPortTimeline())
				&& (shipPortTimelineInputDTO.getFromDate() == null || shipPortTimelineInputDTO.getToDate() == null)) {

			editItineray(shipPortTimelineInputDTO);
		} else {

			editRange(shipPortTimelineInputDTO);
		}

	}

	private void editRange(ShipPortTimelineInputDTO shipPortTimelineInputDTO)
			throws BusinessRequirementViolationException, ElementNotFoundException {
		if (shipPortTimelineInputDTO.getFromDate() == null && shipPortTimelineInputDTO.getToDate() == null) {
			throw new BusinessRequirementViolationException("fromDate and toDate must not be null");
		}

		List<ShipPortTimeline> lista = shipPortTimelineRepository.getShipPortTimelines(
				shipPortTimelineInputDTO.getUiShip(), shipPortTimelineInputDTO.getFromDate(),
				shipPortTimelineInputDTO.getToDate());
		shipPortTimelineInputDTO.setEta(null);
		shipPortTimelineInputDTO.setEtd(null);
		for (ShipPortTimeline spt : lista) {
			updateEntity(spt, shipPortTimelineInputDTO);
		}
		shipPortTimelineRepository.saveAll(lista);
	}

	private void editItineray(ShipPortTimelineInputDTO shipPortTimelineInputDTO)
			throws BusinessRequirementViolationException, ElementNotFoundException {
		if ((shipPortTimelineInputDTO.getEta() == null || shipPortTimelineInputDTO.getEtd() == null)) {
			Optional<PortDTO> port = this.commonService.getPort(shipPortTimelineInputDTO.getUiPort());
			if (port.isPresent() && !port.get().getIso3().equals(this.commonService.getAtSeaPortCode()))
				throw new BusinessRequirementViolationException("eta and etd must not be null");
		}

		Optional<ShipPortTimeline> shipPortTimelineOpt = shipPortTimelineRepository
				.findByUiShipPortTimeline(shipPortTimelineInputDTO.getUiShipPortTimeline());
		if (!shipPortTimelineOpt.isPresent()) {
			throw new ElementNotFoundException("The ShipPortTimeline with uid: "
					+ shipPortTimelineInputDTO.getUiShipPortTimeline() + " is not found.");
		}
		ShipPortTimeline shipPortTimeline = shipPortTimelineOpt.get();
		updateEntity(shipPortTimeline, shipPortTimelineInputDTO);
		shipPortTimelineRepository.save(shipPortTimeline);
	}

	private void updateEntity(ShipPortTimeline spt, ShipPortTimelineInputDTO shipPortTimelineInputDTO)
			throws ElementNotFoundException {
		spt.setCruiseCode(shipPortTimelineInputDTO.getCruiseCode());
		spt.setUiPort(shipPortTimelineInputDTO.getUiPort());
		spt.setEta(shipPortTimelineInputDTO.getEta());
		spt.setEtd(shipPortTimelineInputDTO.getEtd());
		if (shipPortTimelineInputDTO.getPkOperativeStatus() != null) {
			Optional<OperativeStatus> opOpt = operativeStatusRepository
					.findById(shipPortTimelineInputDTO.getPkOperativeStatus());
			if (!opOpt.isPresent()) {
				throw new ElementNotFoundException("The operative Status with id: "
						+ shipPortTimelineInputDTO.getPkOperativeStatus() + " is not found.");
			}
			spt.setOperativeStatus(opOpt.get());
		} else {
			List<OperativeStatus> opList = operativeStatusRepository.findByDescription("Operative");
			if (opList.isEmpty()) {
				throw new ElementNotFoundException("The operative Status Operative is not found.");
			}

			spt.setOperativeStatus(opList.get(0));
		}

		if (shipPortTimelineInputDTO.isHomePort()) {
			spt.setIsHomePort("1");
		} else {
			spt.setIsHomePort("0");
		}

		Optional<Area> areaNOpt = areaRepository.findByUid(shipPortTimelineInputDTO.getUiArea());
		if (!areaNOpt.isPresent()) {
			throw new ElementNotFoundException(
					"The Area with uid: " + shipPortTimelineInputDTO.getUiArea() + " is not found.");
		}
		spt.getItinerary().getSubRegion().getRegion().setArea(areaNOpt.get());

	}

	@Override
	public List<ShipPortTimelineDTO> getShipItineraries(List<String> uiShipList, LocalDate fromDate, LocalDate toDate) {
		List<ShipPortTimelineDTO> result = mapper.shipPortTimelineListToDtoList(!uiShipList.isEmpty()
				? shipPortTimelineRepository.findByUiShipInAndDateBetween(uiShipList, fromDate, toDate)
				: shipPortTimelineRepository.findByDateBetween(fromDate, toDate));

		return this.addPortDetail(result);
	}

	private List<ShipPortTimelineDTO> addPortDetail(List<ShipPortTimelineDTO> shipPortTimelineList) {
		List<PortDTO> ports = commonService.getAllPorts();
		Map<String, PortDTO> mappaResults = ports.stream().collect(Collectors.toMap(PortDTO::getUiPort, x -> x));

		shipPortTimelineList.forEach(spt -> spt.setPort(mappaResults.getOrDefault(spt.getPort().getUiPort(), PortDTO.builder().uiPort(spt.getPort().getUiPort()).build())));

		return shipPortTimelineList;
	}

	@Override
	@Transactional
	public List<ShipPortTimelineDTO> getHistory(String uiShip, String cruiseCode, LocalDate fromDate,
			LocalDate toDate) {
		List<ShipPortTimelineHistory> list = historyRepository.getHistoryByShipAndCriteria(
				uiShip, 
				cruiseCode,
				fromDate != null ? fromDate.atStartOfDay() : null,
				toDate != null ? toDate.atTime(23, 59, 59) : null);
		return this.addPortDetail(mapper.shipPortTimelineHistoryListToDtoList(list));
	}

}
