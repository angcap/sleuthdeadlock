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
 * The persistent class for the Region database table.
 * 
 */
@Entity
@Data

public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkRegion")
	private Short id;

	private String code;

	private String description;

	@Column(name = "uiRegion", columnDefinition = "uniqueidentifier", nullable = false)
	private String uid = UUID.randomUUID().toString();

	// bi-directional many-to-one association to Area
	@ManyToOne
	@JoinColumn(name = "fkArea")
	private Area area;

//	//bi-directional many-to-one association to SubRegion
//	@OneToMany(mappedBy="region")
//	private List<SubRegion> subRegions;

}