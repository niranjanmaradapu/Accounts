/*
 * entity for hsn_details
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hsn_details")
public class HsnDetails implements Serializable {

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
	@Column(name = "is_slab_based")
	private boolean isSlabBased;
	@JoinColumn(name = "tax_id")
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	private Tax tax;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the hsnCode
	 */
	public String getHsnCode() {
		return hsnCode;
	}

	/**
	 * @param hsnCode the hsnCode to set
	 */
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the taxAppliesOn
	 */
	public String getTaxAppliesOn() {
		return taxAppliesOn;
	}

	/**
	 * @param taxAppliesOn the taxAppliesOn to set
	 */
	public void setTaxAppliesOn(String taxAppliesOn) {
		this.taxAppliesOn = taxAppliesOn;
	}

	/**
	 * @return the isSlabBased
	 */
	public boolean isSlabBased() {
		return isSlabBased;
	}

	/**
	 * @param isSlabBased the isSlabBased to set
	 */
	public void setSlabBased(boolean isSlabBased) {
		this.isSlabBased = isSlabBased;
	}

	/**
	 * @return the tax
	 */
	public Tax getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(Tax tax) {
		this.tax = tax;
	}

}
