package online.pelago.p4p.shipitinerary.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;
import online.pelago.p4p.shipitinerary.exceptions.ElementNotFoundException;

public interface ImportExportService {
	
	void exportCsv(PrintWriter writer) throws BusinessRequirementViolationException, IOException, ElementNotFoundException;
	
	List<ImportExportDTO> exportShipPortTimelines() throws BusinessRequirementViolationException, IOException, ElementNotFoundException;

	void importCsv(byte[] file, String username)
			throws BusinessRequirementViolationException, IOException, ElementNotFoundException;
	
}
