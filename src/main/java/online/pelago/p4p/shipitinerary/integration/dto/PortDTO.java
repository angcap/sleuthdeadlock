package online.pelago.p4p.shipitinerary.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortDTO {
	private Short id;
	private String iso3;
	private String description;
	private String uiPort;
     
     private CountryDTO country;
}
