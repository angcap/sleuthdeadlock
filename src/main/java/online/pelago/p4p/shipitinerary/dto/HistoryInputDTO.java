package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class HistoryInputDTO {

	@NotNull
	private String uiShip;
	
	private String cruiseCode;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate fromDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate toDate;
	
}
