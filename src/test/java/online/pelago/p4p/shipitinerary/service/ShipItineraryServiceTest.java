package online.pelago.p4p.shipitinerary.service;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineInputDTO;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;

@SpringBootTest // (webEnvironment = WebEnvironment.RANDOM_PORT)
// (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
// "spring.h2.console.enabled=true" })

@Slf4j
//@WithMockUser(username = "test_user", authorities = { "ADMIN"})
public class ShipItineraryServiceTest {

	@Autowired
	ShipItineraryService shipItineraryService;
	
	@Autowired
	ImportDeltaService deltaService;

	private ObjectMapper mapper = new ObjectMapper();
	
	@MockBean CommonService commonService;

	private List<PortDTO> ports;

	private List<ShipDTO> ships;
	
	@PostConstruct void postConstruct() throws JsonParseException, JsonMappingException, IOException {
		ports = mapper .readValue(this.getClass().getClassLoader().getResource("port.json"),
				new TypeReference<ArrayList<PortDTO>>() {
				});
		ships = mapper.readValue(this.getClass().getClassLoader().getResource("ship.json"),
				new TypeReference<ArrayList<ShipDTO>>() {
				});
		doAnswer(invocationOnMock -> {
			return ports;
		}).when(commonService).getAllPorts();
		
		doAnswer(invocationOnMock -> {
			return ships.stream().filter(s-> s.getUiShip().equalsIgnoreCase(invocationOnMock.getArgument(0)));
		}).when(commonService).getShip(org.mockito.Mockito.anyString());				
		
		doAnswer(invocationOnMock -> {
			return ships;
		}).when(commonService).getAllShips();
		
	}
	
	@Test
	void itShouldGetAllShipItineraries() {

		try {
			assertTrue(!shipItineraryService.getShipItineraries("B3B898C8-F040-476C-8A23-72CEB9FE13D9").isEmpty());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage(), e);
		}

	}
//
	@Test
	void itShouldGetAllShipPortTimeline() {

		try {
			ShipPortTimelineDTO dto = shipItineraryService
					.getShipPortTimeline("94908836-7F24-4A31-8E30-9A205205C6C0");

			log.debug("RES " + dto);
			
			assertTrue(dto != null && Pattern.matches("\\d{2}:\\d{2}",dto.getEta().toString()));


		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage(), e);
		}

	}
	
	@Test
	void itShouldEditShipPortTimeline() {
		
		ShipPortTimelineInputDTO input = new ShipPortTimelineInputDTO();
		//input.setUiShipPortTimeline("8AC0A4E7-1326-44F1-A73D-6AD65D25FA9C");
		input.setCruiseCode("GGGGG");
		input.setUiPort("176D1A10-FD61-46AD-BCBF-FCC85A49A0C3");
		input.setPkOperativeStatus(Short.valueOf("2"));
		input.setUiArea("A47AADDF-B273-4A0C-88DD-56712CBC7B76");
		input.setUiShip("B3B898C8-F040-476C-8A23-72CEB9FE13D9");
		input.setFromDate(LocalDate.parse("2020-04-06",DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		input.setToDate(LocalDate.parse("2020-05-06",DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		try {
			shipItineraryService.updateShipPortTimeline(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void itShouldGetHistory() {
		
		
		try {
			shipItineraryService.getHistory("B3B898C8-F040-476C-8A23-72CEB9FE13D9", null,null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	void itShouldImportDelta() {
		try {
			byte[] fileBytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("itinerary_delta.csv").toURI()));
			deltaService.importDeltaCsv(fileBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
