/*
 * entity for Tax 
*/
package com.otsi.retail.hsnDetails.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tax")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tax implements Serializable {

	/**
	 * @author vasavi
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tax_id")
	private long id;
	@Column(name = "tax_label")
	private String taxLabel;
	@Column(name = "sgst")
	private float sgst;
	@Column(name = "cgst")
	private float cgst;
	@Column(name = "igst")
	private float igst;
	@Column(name = "cess")
	private float cess;
	
	
}
