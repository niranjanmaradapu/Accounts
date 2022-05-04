package com.otsi.retail.hsnDetails.vo;

import com.otsi.retail.hsnDetails.enums.AccountType;
import lombok.Data;

@Data
public class LedgerLogBookVo extends BaseEntityVo {

	private Long ledgerLogBookid;

	private AccountType transactionType;

	private String comments;

	private Long storeId;

	private Long customerId;
	
	private AccountingBookVo accountingBook;
}
