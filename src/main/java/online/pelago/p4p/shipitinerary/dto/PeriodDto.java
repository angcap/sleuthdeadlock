package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PeriodDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate fromDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate toDate;

}
