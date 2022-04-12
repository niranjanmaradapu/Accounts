package com.otsi.retail.paymentgateway.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.paymentgateway.entity.PaymentConfig;
import com.otsi.retail.paymentgateway.vo.PaymentConfigVo;


public interface PaymentConfigService {

	PaymentConfigVo save(PaymentConfigVo paymentConfigVo);

	List<PaymentConfig> getListofData();

	
	void deleteById(Long id);

}
