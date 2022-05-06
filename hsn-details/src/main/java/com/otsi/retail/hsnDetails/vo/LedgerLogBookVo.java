package com.otsi.retail.hsnDetails.vo;

import com.otsi.retail.hsnDetails.enums.AccountStatus;
import com.otsi.retail.hsnDetails.enums.AccountType;
import com.otsi.retail.hsnDetails.enums.PaymentType;
import lombok.Data;

@Data
public class LedgerLogBookVo extends BaseEntityVo {

	private Long ledgerLogBookId;

	private AccountType transactionType;
	
	private AccountType accountType;

	private String comments;

	private Long storeId;

	private Long customerId;
	
	private Long amount;

	private Long accountingBookId;

	private AccountStatus status;

	private PaymentType paymentType;
}
