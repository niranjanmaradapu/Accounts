package com.otsi.retail.hsnDetails.vo;

import com.otsi.retail.hsnDetails.enums.AccountType;

import lombok.Data;

@Data
public class UpdateCreditRequest {

	private Long amount;
	
	private String mobileNumber;
	
	private Long storeId;
	
	private String creditDebit;
	
	private AccountType accountType;

}
