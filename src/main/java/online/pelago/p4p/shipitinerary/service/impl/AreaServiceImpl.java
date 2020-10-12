package online.pelago.p4p.shipitinerary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import online.pelago.p4p.shipitinerary.dto.AreaDTO;
import online.pelago.p4p.shipitinerary.mapper.ShipItineraryMapper;
import online.pelago.p4p.shipitinerary.repository.AreaRepository;
import online.pelago.p4p.shipitinerary.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService{

	@Autowired
	private ShipItineraryMapper mapper;

	@Autowired
	private AreaRepository repository;
	@Override
	public List<AreaDTO> getAllArea() {
		return mapper.map(repository.findAll());
	}
	
}
