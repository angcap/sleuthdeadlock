package online.pelago.p4p.shipitinerary.dto.importexport;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ImportDeltaDTO{
	
	@CsvBindByName(column = "ShipCd", required = true)
	private String shipCd;

	@CsvBindByName(column = "Date", required = true)
	@CsvDate(value = "dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate date;

	@CsvBindByName(column = "ItinCd", required = true)
	private String itinCD;

//	@CsvBindByName(column = "Season", required = true)
//	private String season;

	@CsvBindByName(column = "CruiseNbr", required = true)
	private String cruiseCode;

	@CsvBindByName(column = "MasterCruise", required = false)
	private String masterCruise;
	
	@CsvBindByName(column = "PortCd", required = true)
	private String portCd;

	@CsvBindByName(column = "PortName,Country", required = true)
	private String portNameCountry;

	@CsvBindByName(column = "Position", required = true)
	private String position;

	@CsvBindByName(column = "ETA")
	@CsvDate(value = "Hmm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime eta;

	@CsvBindByName(column = "ETD")
	@CsvDate(value = "Hmm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime etd;
	
	@CsvBindByName(column = "FromToPort")
	private String fromToPort;
	
	@CsvBindByName(column = "PortSeq")
	private String portSeq;
	
	@CsvBindByName(column = "Area", required = true)
	private String area;

	@CsvBindByName(column = "Region", required = true)
	private String region;

	@CsvBindByName(column = "Subregion", required = true)
	private String subRegion;
	
	@CsvBindByName(column = "Status", required = true)
	private String status;
	
	@CsvBindByName(column = "OldItinCd")
	private String oldItinCd;

	@CsvBindByName(column = "OldETA")
	@CsvDate(value = "HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime oldEta;
	
	@CsvBindByName(column = "OldETD")
	@CsvDate(value = "HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime oldEtd;
	
	@CsvBindByName(column = "OldFromToPort")
	private String oldFromToPort;
	
	@CsvBindByName(column = "Change")
	private String change;
	
	private String uiShip;

	private String uiPort;

	private Short fkItinerary;

	private Short fkOperativeStatus;

	private String isHomePort;
	
}
