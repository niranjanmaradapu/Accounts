/**
 * entity for creditdebitnotes
 */
package com.otsi.retail.hsnDetails.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_debit_notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDebitNotes implements Serializable {

	/**
	 * @author vasavi
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long creditDebitId;

	private Long actualAmount;

	private LocalDate createdDate;

	private LocalDate lastModifiedDate;

	private String customerName;

	private String mobileNumber;

	private String comments;

	private Long storeId;

	private Long customerId;

	private String creditDebit;

	private String status;

	private boolean flag;

	private Long transactionAmount;

	private int approvedBy;

}
