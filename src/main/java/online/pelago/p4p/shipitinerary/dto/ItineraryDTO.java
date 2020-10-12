package online.pelago.p4p.shipitinerary.dto;

import lombok.Data;

@Data
public class ItineraryDTO {
	
	private Short id;

	private String code;

	private String season;

	private SubRegionDTO subRegion;

}
