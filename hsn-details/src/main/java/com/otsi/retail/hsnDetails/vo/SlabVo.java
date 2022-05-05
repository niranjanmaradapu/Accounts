/*
 * vo for Slab
*/
package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author vasavi
 *
 */
@ApiModel
@Data
public class SlabVo {

	private long id;
	
	@ApiModelProperty(value = "starting price of the product", name = "priceFrom",required = true)
	private double priceFrom;
	
	@ApiModelProperty(value = "last price of the product", name = "priceTo",required = true)
	private double priceTo;
	
	private Long createdBy;

    private LocalDateTime createdDate;

   
    private Long modifiedBy;

  
    private LocalDateTime lastModifiedDate;
	
	@ApiModelProperty(value = "tax of the product", name = "taxVo",required = true)
	private TaxVo taxVo;

}
