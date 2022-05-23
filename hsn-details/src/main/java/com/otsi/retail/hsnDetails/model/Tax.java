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
/**
 * @author vasavi
 *
 */
@Entity
@Table(name = "tax")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tax extends BaseEntity implements Serializable {

	/**
	 * @author vasavi
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tax_id")
	private Long id;
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
	
	private Long clientId;
	
}
