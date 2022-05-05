/*
 * entity for hsn_details
*/
package com.otsi.retail.hsnDetails.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otsi.retail.hsnDetails.enums.TaxAppliedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hsn_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HsnDetails extends BaseEntity implements Serializable {

	/**
	 * @author vasavi
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hsn_id")
	private long id;
	@Column(name = "hsn_code")
	private String hsnCode;
	@Column(name = "decsription")
	private String description;
	@Column(name = "tax_applies_on")
	private String taxAppliesOn;
	@Column(name = "taxAppliedType")
	@Enumerated(EnumType.STRING)
	private TaxAppliedType taxAppliedType; 
	@JoinColumn(name = "tax_id")
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private Tax tax;
}
