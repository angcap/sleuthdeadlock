package online.pelago.p4p.shipitinerary.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * The persistent class for the Itinerary database table.
 * 
 */
@Entity
@Data
public class Itinerary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkItinerary")
	private Short id;

	private String code;

	private String season;

	@Column(name = "uiItinerary", columnDefinition = "uniqueidentifier", nullable = false)
	private String uid = UUID.randomUUID().toString();

	// bi-directional many-to-one association to SubRegion
	@ManyToOne
	@JoinColumn(name = "fkSubRegion")
	private SubRegion subRegion;

	// bi-directional many-to-one association to ShipPortTimeline
//	@OneToMany(mappedBy="itinerary")
//	private List<ShipPortTimeline> shipPortTimelines;


}