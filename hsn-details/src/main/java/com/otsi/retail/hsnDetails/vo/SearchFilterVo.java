package com.otsi.retail.hsnDetails.vo;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SearchFilterVo {

	private Long storeId;

	private LocalDate fromDate;

	private LocalDate toDate;

	private String mobileNumber;

}
