package online.pelago.p4p.shipitinerary.service;

import java.io.IOException;

import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;



public interface ImportDeltaService {
	
	public void importDeltaCsv(byte[] file) throws BusinessRequirementViolationException, IOException, ElementNotFoundException;
	
}
