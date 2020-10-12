package online.pelago.p4p.shipitinerary.repository;

import java.util.List;
import java.util.Map;

import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;

public interface ImportShipTimelineBulkRepository {

	void save(Map<String, Object>[] shipPortTimelines);

	void save(List<ImportExportDTO> importData, String username);
}
