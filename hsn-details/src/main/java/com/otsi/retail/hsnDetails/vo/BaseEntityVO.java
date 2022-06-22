package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDateTime;
import lombok.Data;
/**
 * @author vasavi
 *
 */
@Data
public class BaseEntityVO {

	private Long createdBy;

	private LocalDateTime createdDate;

	private Long modifiedBy;

	private LocalDateTime lastModifiedDate;

}
