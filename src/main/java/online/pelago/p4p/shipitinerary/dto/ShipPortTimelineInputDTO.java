package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShipPortTimelineInputDTO extends PeriodDto {

	private String uiShipPortTimeline;

	@NotNull
	private String uiShip;
	@NotNull
	private String uiPort;

	private LocalTime eta;

	private LocalTime etd;

	@NotNull
	@Size(max = 5)
	private String cruiseCode;

	@NotNull
	private String uiArea;

	private Short pkOperativeStatus;

	private boolean homePort;

}