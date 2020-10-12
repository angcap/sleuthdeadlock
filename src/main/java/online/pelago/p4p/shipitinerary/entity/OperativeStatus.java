package online.pelago.p4p.shipitinerary.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the OperativeStatus database table.
 * 
 */
@Entity
@Data
public class OperativeStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkOperativeStatus")
	private Short id;

	private String description;

	public static final String OPERATIVE = "Operative";

	
	
}