package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CreditDebitNotesVo {

	private Long creditDebitId;

	@ApiModelProperty(value = "actual amount of the c/d customer", name = "actualAmount",required = true)
	private Long actualAmount;

	@ApiModelProperty(value = "created date of the c/d customer", name = "fromDate")
	private LocalDate fromDate;

	@ApiModelProperty(value = "last modified date of the c/d person", name = "toDate")
	private LocalDate toDate;

	@ApiModelProperty(value = "name of the customer", name = "customerName",required = true)
	private String customerName;

	@ApiModelProperty(value = "mobile number of the customer", name = "customerName",required = true)
	private String mobileNumber;

	@ApiModelProperty(value = "comments of the customer if c/d", name = "comments",required = true)
	private String comments;

	@ApiModelProperty(value = "storeId of teh customer", name = "storeId",required = true)
	private Long storeId;

	@ApiModelProperty(value = "id of the customer", name = "customerId",required = true)
	private Long customerId;

	@ApiModelProperty(value = "c/d of the customer", name = "creditDebit",required = true)
	private String creditDebit;

	@ApiModelProperty(value = "status(credited,debited,used) of the customer", name = "status")
	private String status;

	@ApiModelProperty(value = "flag(true/false) of the customer", name = "flag")
	private boolean flag;

	@ApiModelProperty(value = "transaction amount of the customer", name = "transactionAmount",required = true)
	private Long transactionAmount;

	@ApiModelProperty(value = "id of the employee", name = "approvedBy",required = true)
	private int approvedBy;
}
