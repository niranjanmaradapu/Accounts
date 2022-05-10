package com.otsi.retail.paymentgateway.vo;

import java.util.Date;

import org.springframework.stereotype.Component;



import lombok.Data;

@Component
@Data
public class AccountVo {
	
	private int amount;
	
	private String referenceNumber;
	
	
	
	private String type;

		
	

}
