package online.pelago.p4p.shipitinerary.integration.dto;

import lombok.Data;

@Data
public class CountryDTO {
    private Short id;
    private String iso2;
    private String iso3;
    private String uiCountry;
    private String description;
}
