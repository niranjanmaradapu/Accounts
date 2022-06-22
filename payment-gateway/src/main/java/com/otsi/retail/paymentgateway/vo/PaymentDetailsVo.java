package com.otsi.retail.paymentgateway.vo;

import lombok.Data;

@Data

public class PaymentDetailsVo {

	private String ReferenceNumber;
	
	private String razorPayId;
	
	private Long amount;
	
	private String payType;
	
	private Object Order;
	
	private Long storeId;
	
	private Long customerId;

}
