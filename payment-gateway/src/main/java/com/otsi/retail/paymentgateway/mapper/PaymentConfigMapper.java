package com.otsi.retail.paymentgateway.mapper;

import org.springframework.stereotype.Component;

import com.otsi.retail.paymentgateway.entity.PaymentConfig;
import com.otsi.retail.paymentgateway.vo.PaymentConfigVo;

@Component
public class PaymentConfigMapper {
	
	public PaymentConfig  paymentConfigVoToEntity(PaymentConfigVo paymentConfigVo) {
		
		PaymentConfig paymentConfig = new PaymentConfig();
		
		paymentConfig.setAccessKey(paymentConfigVo.getAccessKey());
		paymentConfig.setSecretKey(paymentConfigVo.getSecretKey());
		paymentConfig.setMobileNumber(paymentConfigVo.getMobileNumber());
		paymentConfig.setActive(paymentConfigVo.getActive());
		
		return paymentConfig;
	}
	
	 public PaymentConfigVo paymentConfigEntityToVo(PaymentConfig paymentConfig) {
		 
		   PaymentConfigVo paymentConfigVo = new PaymentConfigVo();
		   
		   paymentConfigVo.setAccessKey(paymentConfig.getAccessKey());
		   paymentConfigVo.setMobileNumber(paymentConfig.getMobileNumber());
		   paymentConfigVo.setSecretKey(paymentConfig.getSecretKey());
		   paymentConfigVo.setActive(paymentConfig.getActive());
		   
		 
		 return paymentConfigVo;
				 
		 
	 }

}
