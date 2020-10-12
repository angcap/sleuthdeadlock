package online.pelago.p4p.shipitinerary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import online.pelago.p4p.shipitinerary.dto.AreaDTO;
import online.pelago.p4p.shipitinerary.dto.ItineraryDTO;
import online.pelago.p4p.shipitinerary.dto.RegionDTO;
import online.pelago.p4p.shipitinerary.dto.ShipPortTimelineDTO;
import online.pelago.p4p.shipitinerary.dto.SubRegionDTO;
import online.pelago.p4p.shipitinerary.dto.importexport.ImportDeltaDTO;
import online.pelago.p4p.shipitinerary.dto.importexport.ImportExportDTO;
import online.pelago.p4p.shipitinerary.entity.Area;
import online.pelago.p4p.shipitinerary.entity.ImportShipTimeline;
import online.pelago.p4p.shipitinerary.entity.Itinerary;
import online.pelago.p4p.shipitinerary.entity.Region;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimeline;
import online.pelago.p4p.shipitinerary.entity.ShipPortTimelineHistory;
import online.pelago.p4p.shipitinerary.entity.SubRegion;

@Mapper(componentModel = "spring", uses = { BooleanMapper.class })
public interface ShipItineraryMapper {

	AreaDTO map(Area area);

	List<AreaDTO> map(List<Area> areas);

	RegionDTO map(Region region);

	SubRegionDTO map(SubRegion subRegion);

	ItineraryDTO map(Itinerary itinerary);

	@Mapping(target = "port.uiPort", source = "uiPort")
	ShipPortTimelineDTO shipPortTimelineToDTO(ShipPortTimeline shipPortTimeline);

	List<ShipPortTimelineDTO> shipPortTimelineListToDtoList(List<ShipPortTimeline> shipPortTimelineList);

	@Mapping(target = "port.uiPort", source = "uiPort")
	ShipPortTimelineDTO shipPortTimelineHistoryToDTO(ShipPortTimelineHistory shipPortTimelineHistory);

	List<ShipPortTimelineDTO> shipPortTimelineHistoryListToDtoList(
			List<ShipPortTimelineHistory> shipPortTimelineHistoryList);

	@Mapping(target = "operativeStatus.id", source = "fkOperativeStatus")
	@Mapping(target = "itinerary.id", source = "fkItinerary")
	ShipPortTimeline importExportDTOToEntity(ImportExportDTO importExportDTO);

	List<ShipPortTimeline> importExportDTOListToEntityList(List<ImportExportDTO> importExportList);

	@Mapping(target = "operativeStatus.id", source = "fkOperativeStatus")
	@Mapping(target = "itinerary.id", source = "fkItinerary")
	ShipPortTimeline importDeltaDTOToEntity(ImportDeltaDTO importDeltaDTO);

	List<ShipPortTimeline> importDeltaDTOListToEntityList(List<ImportDeltaDTO> importDeltaList);

	@Mapping(target = "itineryCode", source = "itinCD")
	@Mapping(target = "portCode", source = "portCd")
	@Mapping(target = "shipCode", source = "shipCd")
	ImportShipTimeline importExportDTOToImportEntity(ImportExportDTO importExportDTO);
	

	List<ImportShipTimeline> importExportDTOToImportEntityList(List<ImportExportDTO> importExportDTOList);
	
	

	@Mapping(target = "itineryCode", source = "itinCD")
	@Mapping(target = "portCode", source = "portCd")
	@Mapping(target = "shipCode", source = "shipCd")
	@Mapping(target = "oldItineryCode", source = "oldItinCd")
	
	ImportShipTimeline importDeltaDTOToImportEntity(ImportDeltaDTO importDeltaDTO);
	

	List<ImportShipTimeline> importDeltaDTOToImportEntityList(List<ImportDeltaDTO> importDeltaDTOList);
	
	
}