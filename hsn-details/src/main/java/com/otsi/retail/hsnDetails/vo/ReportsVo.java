package com.otsi.retail.hsnDetails.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ReportsVo {

	@ApiModelProperty(value = "id of the store", name = "storeId",required = true)
	private Long storeId;

	@ApiModelProperty(value = "amount of the customer either c/d", name = "actualAmount",required = true)
	private Long amount;

	@ApiModelProperty(value = "trnsaction amount of the customer either c/d", name = "transactionAmount",required = true)
	private Long usedAmount;
	
	@ApiModelProperty(value = "name of the store", name = "name",required = true)
	private String name;

}
