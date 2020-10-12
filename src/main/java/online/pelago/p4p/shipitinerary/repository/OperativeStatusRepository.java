package online.pelago.p4p.shipitinerary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import online.pelago.p4p.shipitinerary.entity.OperativeStatus;

public interface OperativeStatusRepository extends JpaRepository<OperativeStatus, Short>{
	
	public List<OperativeStatus> findByDescription(String description);

}
