package com.otsi.retail.paymentgateway.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class PaymentLinkRequest {
	
	private Long amount;
	
	private String description;

	private CustomerDetailsRequest customerDetails;
	
}
