package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreditDebitNotesVo {

	private Long creditDebitId;

	private Long actualAmount;

	private LocalDate fromDate;

	private LocalDate toDate;

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
