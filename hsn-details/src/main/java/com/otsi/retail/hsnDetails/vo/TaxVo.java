package com.otsi.retail.hsnDetails.vo;
/*
 * vo for Tax
*/
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author vasavi
 *
 */

@ApiModel
@Data
public class TaxVo {

	private Long id;
	
	@ApiModelProperty(value = "product name of the textile product", name = "name",required = true)
	private String taxLabel;
	
	@ApiModelProperty(value = "product name of the textile product", name = "name",required = true)
	private float sgst;
	
	@ApiModelProperty(value = "product name of the textile product", name = "name",required = true)
	private float cgst;
	
	@ApiModelProperty(value = "product name of the textile product", name = "name",required = true)
	private float igst;
	
	@ApiModelProperty(value = "product name of the textile product", name = "name",required = true)
	private float cess;
	

}
