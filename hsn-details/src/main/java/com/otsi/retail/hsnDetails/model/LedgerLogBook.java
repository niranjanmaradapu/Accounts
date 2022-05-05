package com.otsi.retail.hsnDetails.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otsi.retail.hsnDetails.enums.AccountStatus;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "ledger_log_book")
public class LedgerLogBook extends BaseEntity {

	@Id
	@GeneratedValue
	private Long ledgerLogBookid;

	@Enumerated(EnumType.STRING)
	private AccountType transactionType;

	private String comments;

	private Long storeId;

	private Long customerId;

	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	@ManyToOne
	@JoinColumn(name = "accountingBookId")
	private AccountingBook accountingBook;

}
