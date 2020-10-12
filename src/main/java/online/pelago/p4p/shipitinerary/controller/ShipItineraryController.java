package online.pelago.p4p.shipitinerary.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.dto.AreaDTO;
import online.pelago.p4p.shipitinerary.dto.AsynchronousTask;
import online.pelago.p4p.shipitinerary.dto.HistoryInputDTO;
import online.pelago.p4p.shipitinerary.dto.PeriodDto;
import online.pelago.p4p.shipitinerary.dto.SearchRequestDTO;
import online.pelago.p4p.shipitinerary.dto.ShipItineraryDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineInputDTO;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;
import online.pelago.p4p.shipitinerary.service.AreaService;
import online.pelago.p4p.shipitinerary.service.AsyncImportService;
import online.pelago.p4p.shipitinerary.service.ImportDeltaService;
import online.pelago.p4p.shipitinerary.service.ImportExportService;
import online.pelago.p4p.shipitinerary.service.ShipItineraryService;
import online.pelago.p4p.shipitinerary.service.SseService;

@RestController
@RequestMapping("ship-itinerary")
@Slf4j
public class ShipItineraryController implements AuthenticationAwareController {
	
	public static final String CSVFILENAME = "download.csv";
	public static final String HEADERKEY = "Content-Disposition";
	public static final String HEADERVALUE = String.format("attachment; filename=\"%s\"", CSVFILENAME);

	@Autowired
	@Lazy
	private ShipItineraryService shipItineraryService;

	@Autowired
	@Lazy
	private ImportExportService importExportService;
	
	@Autowired
	@Lazy
	private AsyncImportService asyncImportService;

	@Autowired
	@Lazy
	private ImportDeltaService importDeltaService;

	@Autowired
	@Lazy
	private AreaService areaService;
	
	@Autowired
	@Lazy
	private SseService sseService;

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "/{uiShip}")
	@Operation(summary = "The method returns ship itineraries", description = "The method returns ship itineraries.")
	public ResponseEntity<List<ShipItineraryDTO>> getShipItineraries(
			@Valid @NotNull @PathVariable("uiShip") String uiShip) {
		return new ResponseEntity<>(shipItineraryService.getShipItineraries(uiShip), HttpStatus.OK);
	}

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "/search")
	@Operation(summary = "The method returns ship itineraries", description = "The method returns ship itineraries.")
	public ResponseEntity<List<ShipPortTimelineDTO>> getShipItineraries(@Valid SearchRequestDTO searchRequest) {
		List<String> uiship = Collections.emptyList();
		if (searchRequest.getUiShip() != null) {
			uiship = searchRequest.getUiShip();
		}
		return new ResponseEntity<>(
				shipItineraryService.getShipItineraries(uiship, searchRequest.getFromDate(), searchRequest.getToDate()),
				HttpStatus.OK);
	}

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "ship/{uiShip}/port-timeline")
	@Operation(summary = "The method returns all Ship Port Timeline", description = "The method returns all Ship Port Timeline")
	public ResponseEntity<List<ShipPortTimelineDTO>> getShipPortTimelines(
			@Valid @NotNull @PathVariable("uiShip") String uiShip, @RequestBody @Valid PeriodDto inputDate)
			throws ElementNotFoundException {

		return new ResponseEntity<>(
				shipItineraryService.getShipPortTimelines(uiShip, inputDate.getFromDate(), inputDate.getToDate()),
				HttpStatus.OK);
	}

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "port-timeline/{ui}")
	@Operation(summary = "The method returns the Ship Port Timeline with specified ui", description = "The method returns the Ship Port Timeline with specified ui")
	public ResponseEntity<ShipPortTimelineDTO> getShipPortTimelineByUi(@Valid @NotNull @PathVariable("ui") String ui)
			throws ElementNotFoundException {

		return new ResponseEntity<>(shipItineraryService.getShipPortTimeline(ui), HttpStatus.OK);
	}

	@CrossOrigin(methods = RequestMethod.PUT)
	@PutMapping(value = "port-timeline")
	@Operation(summary = "The method edit the Ship Port Timeline", description = "The method edit the Ship Port Timeline")
	public void editShipPortTimeline(@RequestBody @NotNull @Valid ShipPortTimelineInputDTO data)
			throws ElementNotFoundException, BusinessRequirementViolationException {
		shipItineraryService.updateShipPortTimeline(data);

	}

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "port-timeline/history")
	@Operation(summary = "The method returns the Ship Port Timeline history", description = "The method returns the Ship Port Timeline history")
	public ResponseEntity<List<ShipPortTimelineDTO>> getHistory(@Valid HistoryInputDTO data) {

		return new ResponseEntity<>(shipItineraryService.getHistory(data.getUiShip(), data.getCruiseCode(),
				data.getFromDate(), data.getToDate()), HttpStatus.OK);

	}

	@CrossOrigin(methods = RequestMethod.POST)
	@Operation(summary = "The method imports the specified Ship Itinerary.", description = "The method imports the specified Ship Itinerary using SSE Events")
	@PostMapping(path = "port-timeline/import")
	public SseEmitter importShipItinerary(@RequestParam("file") MultipartFile file)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		log.debug("importing file {}",file.getOriginalFilename());		
		SseEmitter sseEmitter = new SseEmitter();
		this.sseService.add(
				new AsynchronousTask<Void>(sseEmitter, asyncImportService.importCsv(file.getBytes(), getRemoteUser())));
		return sseEmitter;
	}
	
	@CrossOrigin(methods = RequestMethod.GET)
	@Operation(summary = "The method exports the Ship Itinerary in csv format", description = "The method exports the Ship Itinerary in csv format")
	@GetMapping(path = "port-timeline/export", produces = "text/csv")
	public void exportShipItinerary(
			HttpServletResponse response
			)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		response.setHeader(HEADERKEY, HEADERVALUE);
		importExportService.exportCsv(response.getWriter());
	}

	@CrossOrigin(methods = RequestMethod.GET)
	@GetMapping(value = "area")
	@Operation(summary = "The method returns all areas", description = "The method returns all areas")
	public List<AreaDTO> getAllArea() {
		return areaService.getAllArea();
	}
	
	@CrossOrigin(methods = RequestMethod.POST)
	@Operation(summary = "The method imports the specified Ship Itinerary DELTA file.", description = "The method imports the specified Ship Itinerary file according to DELTA import file format.")
	@PostMapping(path = "port-timeline/import/delta")
	public ResponseEntity<Boolean> importDeltaShipItinerary(@RequestParam("file") MultipartFile file)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException {
		log.debug(file.getOriginalFilename());
		importDeltaService.importDeltaCsv(file.getBytes());
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
