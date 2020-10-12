package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import online.pelago.p4p.shipitinerary.integration.dto.PortDTO;

@Data
public class ShipPortTimelineDTO {

	private Integer id;

	private String cruiseCode;

	private LocalDate date;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime eta;

	@JsonFormat(pattern = "HH:mm")
	private LocalTime etd;

	private OperativeStatusDTO operativeStatus;

	private String fkUsername;

	private LocalDateTime utcUpdate;

	private String position;

	private String uiShip;

	private String uiShipPortTimeline;

	private ItineraryDTO itinerary;

	private PortDTO port;

	private Boolean isHomePort;

}
