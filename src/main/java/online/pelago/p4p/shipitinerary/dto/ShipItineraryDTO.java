package online.pelago.p4p.shipitinerary.dto;

import java.time.LocalDate;

public interface ShipItineraryDTO {
	
	LocalDate getStartDate();
	
	LocalDate getEndDate();
	
	String getItineraryCode();
	
	String getAreaDescription();
	
	String getAreaCode();
	
	String getRegionCode();
	
	String getRegionDescription();
	
    String getSubRegionCode();
	
	String getSubRegionDescription();
	
	String getColor();

}
