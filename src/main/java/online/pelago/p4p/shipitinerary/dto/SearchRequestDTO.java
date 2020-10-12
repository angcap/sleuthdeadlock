package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SearchRequestDTO {

	private List<String> uiShip;
	@NotNull(message = "fromDate must not be null") @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate fromDate;
	@NotNull(message = "toDate must not be null") @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate toDate;
}
