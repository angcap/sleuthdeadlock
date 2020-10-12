package online.pelago.p4p.shipitinerary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.pelago.p4p.shipitinerary.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Short>{
	

	public Optional<Area> findByUid(String uiArea);
	
}
