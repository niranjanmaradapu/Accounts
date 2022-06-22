package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDateTime;

import com.otsi.retail.hsnDetails.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @author vasavi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountingBookVO extends BaseEntityVO {

	private Long accountingBookId;

	private Long amount;

	private Long storeId;

	private Long customerId;

	private AccountType accountType;

	private Long createdBy;

	private LocalDateTime createdDate;

	private Long modifiedBy;

	private LocalDateTime lastModifiedDate;
	
	private String customerName;
	
	private String mobileNumber;
	
	private Long usedAmount;
	
	private Long balanceAmount;
	
	private Long clientId;
}
