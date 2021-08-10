/**
  * Entity for TaxModel
*/
package com.otsi.kalamandir.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tax_master")
public class TaxModel  implements Serializable{

	/**
	 * @author Lakshmi
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String taxLable;
	private float sgst;
	private float cgst;
	private float igst;
	private float cess;
	
	
	
	/**
	 * default constructor
	 */
	public TaxModel() {
		super();
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the taxLable
	 */
	public String getTaxLable() {
		return taxLable;
	}
	/**
	 * @param taxLable the taxLable to set
	 */
	public void setTaxLable(String taxLable) {
		this.taxLable = taxLable;
	}
	/**
	 * @return the sgst
	 */
	public float getSgst() {
		return sgst;
	}
	/**
	 * @param sgst the sgst to set
	 */
	public void setSgst(float sgst) {
		this.sgst = sgst;
	}
	/**
	 * @return the cgst
	 */
	public float getCgst() {
		return cgst;
	}
	/**
	 * @param cgst the cgst to set
	 */
	public void setCgst(float cgst) {
		this.cgst = cgst;
	}
	/**
	 * @return the igst
	 */
	public float getIgst() {
		return igst;
	}
	/**
	 * @param igst the igst to set
	 */
	public void setIgst(float igst) {
		this.igst = igst;
	}
	/**
	 * @return the cess
	 */
	public float getCess() {
		return cess;
	}
	/**
	 * @param cess the cess to set
	 */
	public void setCess(float cess) {
		this.cess = cess;
	}

	
}
