package online.pelago.p4p.shipitinerary.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.extern.slf4j.Slf4j;
import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;

@Slf4j
public abstract class AbstractBulkRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${bulk_operations.shipitinerary.batch_size:50000}")
	private Integer batchSize;
	
	public abstract String getBatchUpdateDml();
	public abstract Map<String,Object> getBatchUpdateParametersMap(ImportExportDTO importData, String username, LocalDateTime utcUpdate);
	
	public void save(Map<String, Object>[] shipPortTimelines) {
		try {
			jdbcTemplate.batchUpdate(getBatchUpdateDml(),
					shipPortTimelines);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void save(List<ImportExportDTO> importData, String username) {

		List<Map<String, Object>> params = new ArrayList<>();
		int cursor = -1;
		LocalDateTime now = LocalDateTime.now();
		for (ImportExportDTO itineray : importData) {
			cursor++;
			params.add(getBatchUpdateParametersMap(itineray,username,now));
			if (cursor > this.batchSize) {
				log.info("bulk insert {} shipPortTimeline records", params.size());
				this.save(params.toArray(new HashMap[params.size()]));
				cursor = 0;
				params.clear();
			}
		}

		log.info("bulk insert {} shipPortTimeline", params.size());
		this.save(params.toArray(new HashMap[params.size()]));
		params.clear();

	}	

}
