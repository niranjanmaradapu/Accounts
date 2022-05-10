/*
 * entity for Slab 
*/
package com.otsi.retail.hsnDetails.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "slab")
public class Slab extends BaseEntity implements Serializable {
	/**
	 * @author vasavi
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "price_from")
	private double priceFrom;
	@Column(name = "price_to")
	private double priceTo;
	@JoinColumn(name = "tax_id")
	@OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
	private Tax tax;
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "hsn_id")
	private HsnDetails hsnDetails;

}
