package com.otsi.retail.paymentgateway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentConfigVo {

	private Long id;
	
	private String secretKey;
	
	private String accessKey;
	
	private Boolean active;
	
	private String mobileNumber;
	
	

}
