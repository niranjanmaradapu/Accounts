package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;

import com.otsi.retail.hsnDetails.enums.AccountType;

import lombok.Data;

@Data
public class SearchFilterVO {

	private Long storeId;

	private LocalDate fromDate;

	private LocalDate toDate;

	private String mobileNumber;
	
	private AccountType accountType;
	
	private Long customerId;

}
