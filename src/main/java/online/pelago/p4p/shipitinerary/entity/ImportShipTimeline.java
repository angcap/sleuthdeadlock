package online.pelago.p4p.shipitinerary.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;


/**
 * The persistent class for the ImportShipTimeline database table.
 * 
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class ImportShipTimeline implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkImportShipTimeline")
	private Integer id;

	private String area;

	private String change;

	private String cruiseCode;
	
	@Column(name="\"date\"")
	private LocalDate date;

	private LocalTime eta;

	private LocalTime etd;

	private String itineryCode;

	private LocalTime oldEta;

	private LocalTime oldEtd;

	private String oldItineryCode;

	private String operativeStatus;

	private String portCode;

	private String portNameCountry;

	private String position;

	private String region;

	private String season;

	private String shipCode;

	private String subRegion;
	
	@LastModifiedDate
	@CreatedDate
	private LocalDateTime utcDate;
	
	@LastModifiedDate
	@CreatedDate
	private Timestamp utcProcessed;

	

}