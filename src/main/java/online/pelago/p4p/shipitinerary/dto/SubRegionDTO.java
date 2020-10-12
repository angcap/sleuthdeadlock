package online.pelago.p4p.shipitinerary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubRegionDTO extends AreaDTO {

	private RegionDTO region;
	private String color;

}
