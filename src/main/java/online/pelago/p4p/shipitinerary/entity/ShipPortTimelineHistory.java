package online.pelago.p4p.shipitinerary.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ShipPortTimelineHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkShipPortTimelineHistory")
	private Integer id;
	
	private Integer pkShipPortTimeline;
	
	private String cruiseCode;

	@Column(name="\"date\"")
	private LocalDate date;

	private LocalTime eta;

	private LocalTime etd;

	@OneToOne()
	@JoinColumn(name = "fkOperativeStatus")
	private OperativeStatus operativeStatus;
	
	@LastModifiedBy
	@CreatedBy
	private String fkUsername;

	private LocalDateTime utcUpdate;

	@Column(name="\"position\"")
	private String position;

	private String uiPort;

	
	private String uiShip;

	
	private String uiShipPortTimeline;
	
	private String isHomePort;
			
	// bi-directional many-to-one association to Itinerary
	@ManyToOne
	@JoinColumn(name = "fkItinerary")
	private Itinerary itinerary;
	
	
	public ShipPortTimelineHistory(ShipPortTimeline t, LocalDateTime now) {
		this.pkShipPortTimeline = t.getId();
		org.springframework.beans.BeanUtils.copyProperties(t,this,"className","id");
		this.setUtcUpdate(now);
	}
	


}
