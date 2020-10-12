package online.pelago.p4p.shipitinerary.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * The persistent class for the ShipPortTimeline database table.
 * 
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class ShipPortTimeline implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkShipPortTimeline")
	private Integer id;

	@Column(length = 5, nullable = false)
	private String cruiseCode;

	@Column(name="\"date\"",updatable = false)
	private LocalDate date;

	private LocalTime eta;

	private LocalTime etd;

	@OneToOne()
	@JoinColumn(name = "fkOperativeStatus")
	private OperativeStatus operativeStatus;
	
	@LastModifiedBy
	@CreatedBy
	private String fkUsername;

	@LastModifiedDate
	@CreatedDate
	private LocalDateTime utcUpdate;

	@Column(name="\"position\"")
	private String position;

	private String uiPort;

	@Column(updatable = false)
	private String uiShip;

	@Column(columnDefinition = "uniqueidentifier", nullable = false, updatable = false)
	private String uiShipPortTimeline = UUID.randomUUID().toString();
	
	private String isHomePort;
			
	// bi-directional many-to-one association to Itinerary
	@ManyToOne
	@JoinColumn(name = "fkItinerary")
	private Itinerary itinerary;


	public String getItineraryCode() {
		return getItinerary().getCode();
	}



}