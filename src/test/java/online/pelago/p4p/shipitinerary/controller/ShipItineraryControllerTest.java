package online.pelago.p4p.shipitinerary.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;
import online.pelago.p4p.shipitinerary.integration.dto.ShipDTO;
import online.pelago.p4p.shipitinerary.integration.service.CommonService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "testdb=ShipItineraryControllerTest" })
@AutoConfigureMockMvc
@Slf4j
public class ShipItineraryControllerTest extends SecurityAwareControllerTest {

	@MockBean CommonService commonService;

	private List<PortDTO> ports;

	private List<ShipDTO> ships;
	
	private LocalDate fromDate = LocalDate.of(2020, 1, 1);
	private LocalDate toDate = LocalDate.of(2020, 6, 1);
	
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
	void itShouldGetMultipleShipTimeline() {
		try {
			mockMvc.perform(authorized(get("/ship-itinerary/search"))
					.queryParam("uiShip", "B3B898C8-F040-476C-8A23-72CEB9FE13D9")					
					.queryParam("fromDate", fromDate.toString())
					.queryParam("toDate", toDate.toString())
					.accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}
	
	@Test
	void itShouldGetAllTimeline() {
		try {
			mockMvc.perform(authorized(get("/ship-itinerary/search"))
					.queryParam("fromDate", fromDate.toString())
					.queryParam("toDate", toDate.toString())
					.accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}

	@Test
	@Transactional
	@Rollback
	void itShouldImport() {
		try {
			MockMultipartFile itinerary_full = new MockMultipartFile("file", "filename.csv", "text/csv", Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("itinerary_full.csv").toURI())));
			mockMvc
				.perform(
						authorized(
								MockMvcRequestBuilders.multipart("/ship-itinerary/port-timeline/import").file(itinerary_full)))
			.andDo(print())
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}

	@Test
	void itShouldGetAllArea(){
		try {
			mockMvc.perform(authorized(get("/ship-itinerary/area"))
					.accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}
	@Test
	void itShouldExportCsv() {
		try {
			mockMvc.perform(authorized(get("/ship-itinerary/port-timeline/export").accept("text/csv"))).andDo(print())
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}
	
}
