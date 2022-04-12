package com.otsi.retail.paymentgateway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.paymentgateway.entity.PaymentConfig;
import com.otsi.retail.paymentgateway.gatewayresponse.PaymentConfigRepository;
import com.otsi.retail.paymentgateway.mapper.PaymentConfigMapper;
import com.otsi.retail.paymentgateway.vo.PaymentConfigVo;

@Service
public class PaymentConfigServiceImpl implements PaymentConfigService {

	@Autowired
	private PaymentConfigRepository paymentConfigRepositoty;
	
	@Autowired
	private PaymentConfigMapper paymentConfigMapper;

	@Override
	public PaymentConfigVo save(PaymentConfigVo paymentConfigVo) {
		
		PaymentConfig paymentConfigVoToEntity= null;
		PaymentConfigVo vo=null;
		if(paymentConfigVo != null) {
			paymentConfigVoToEntity  = paymentConfigMapper.paymentConfigVoToEntity(paymentConfigVo);
		}
		
		  PaymentConfig savePaymentConfig = paymentConfigRepositoty.save(paymentConfigVoToEntity);
		  if(savePaymentConfig !=null)
			  paymentConfigMapper.paymentConfigEntityToVo(savePaymentConfig);
		return vo;
	}

	@Override
	public List<PaymentConfig> getListofData() {
		return paymentConfigRepositoty.findAll();

	}

	@Override
	public void deleteById(Long id) {
		
		Optional<PaymentConfig> optPaymentConfig = paymentConfigRepositoty.findById(id);
		  if(optPaymentConfig.isPresent()) {
			PaymentConfig paymentConfig = optPaymentConfig.get();
			paymentConfig.setActive(true);
			paymentConfigRepositoty.save(paymentConfig);
		}else {
			System.out.println("Id is unknown");
			
		}
		
	}

}
