package online.pelago.p4p.shipitinerary.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;


/**
 * The persistent class for the Area database table.
 * 
 */
@Entity
@Data
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkArea")
	private Short id;

	private String code;

	private String description;
	
	@Column(name = "uiArea", columnDefinition = "uniqueidentifier", nullable = false)
	private String uid;

//	//bi-directional many-to-one association to Region
//	@OneToMany(mappedBy="area")
//	private List<Region> regions;


	

//	public Region addRegion(Region region) {
//		getRegions().add(region);
//		region.setArea(this);
//
//		return region;
//	}
//
//	public Region removeRegion(Region region) {
//		getRegions().remove(region);
//		region.setArea(null);
//
//		return region;
//	}

}