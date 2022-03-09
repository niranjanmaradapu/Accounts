/**
 * vo for HsnDetails
 */
package com.otsi.retail.hsnDetails.vo;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author vasavi
 *
 */
@ApiModel
@Data
public class HsnDetailsVo {

	private long id;
	
	@ApiModelProperty(value = "hsn code of the product", name = "hsnCode",required = true)
	private String hsnCode;

	@ApiModelProperty(value = "goods/services of the product", name = "description",required = true)
	private String description;

	@ApiModelProperty(value = "netPrice/rsp of the product", name = "taxAppliesOn",required = true)
	private String taxAppliesOn;

	@ApiModelProperty(value = "SlabBased of the product", name = "isSlabBased",required = true)
	private boolean isSlabBased;

	@ApiModelProperty(value = "tax of the product", name = "taxVo",required = true)
	private TaxVo taxVo;

	@ApiModelProperty(value = "slab ranges of the product", name = "slabVos",required = true)
	private List<SlabVo> slabVos;

	
}
