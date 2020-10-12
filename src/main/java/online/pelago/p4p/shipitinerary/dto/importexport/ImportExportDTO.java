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
public class ImportExportDTO{
	
	public static final String[] FIELDS_ORDER = { "ShipCd", "Date", "ItinCD", "Season", "CruiseNbr", "PortCd",
			"PortNameCountry", "Position", "ETA", "ETD", "Area", "Region", "Subregion" };

	// ShipCd: it is the Ship Code
	// - Date: it is the date when the itinineray change will happens in the format
	// DD/MM/YY
	// - ItinCD: it is the Itinerary Code
	// - Season: the season of the cruise
	// - CruiseNbr: it is the Cruise number
	// - PortCd: it is the Port Code
	// - PortName,Country: it is the code made by Port and Country
	// - Position: it is the position of the ship in the port (Alongside, Anchorage,
	// Along/Anch, At Sea)
	// - ETA: it is the estimate time of arrival in Port local hours in the format
	// HH:MM
	// - ETD: it is the estimate time of departure in Port local hours in the format
	// HH:MM
	// - Area: Geographic position
	// - Region: Geographic position
	// - Subregion: Geographic position

	@CsvBindByName(column = "ShipCd", required = true)
	private String shipCd;

	@CsvBindByName(column = "Date", required = true)
	@CsvDate(value = "dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate date;

	@CsvBindByName(column = "ItinCD", required = true)
	private String itinCD;

	@CsvBindByName(column = "Season", required = true)
	private String season;

	@CsvBindByName(column = "CruiseNbr", required = true)
	private String cruiseCode;

	@CsvBindByName(column = "PortCd", required = true)
	private String portCd;

	@CsvBindByName(column = "PortNameCountry", required = true)
	private String portNameCountry;

	@CsvBindByName(column = "Position", required = true)
	private String position;

	@CsvBindByName(column = "ETA")
	@CsvDate(value = "HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime eta;

	@CsvBindByName(column = "ETD")
	@CsvDate(value = "HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime etd;

	@CsvBindByName(column = "Area", required = true)
	private String area;

	@CsvBindByName(column = "Region", required = true)
	private String region;

	@CsvBindByName(column = "Subregion", required = true)
	private String subRegion;

	private String uiShip;

	private String uiPort;

	private Short fkItinerary;

	private Short fkOperativeStatus;

	private String isHomePort;

}
