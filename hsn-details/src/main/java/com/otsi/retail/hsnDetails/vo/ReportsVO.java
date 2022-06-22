package com.otsi.retail.hsnDetails.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ReportsVO {

	@ApiModelProperty(value = "debit amount of the customer", name = "dAmount",required = true)
	private Long dAmount;

	@ApiModelProperty(value = "id of the store", name = "storeId",required = true)
	private Long storeId;

	@ApiModelProperty(value = "amount of the customer either c/d", name = "actualAmount",required = true)
	private Long actualAmount;

	@ApiModelProperty(value = "trnsaction amount of the customer either c/d", name = "transactionAmount",required = true)
	private Long transactionAmount;
	
	@ApiModelProperty(value = "name of the store", name = "name",required = true)
	private String name;

}
