package com.otsi.retail.paymentgateway.vo;

import lombok.Data;

@Data
public class PaymentDetailsVo {

	private String newsaleOrder;
	
	private String razorPayId;
	
	private Long amount;
	
	private String payType;
	
	private Object Order;

}
