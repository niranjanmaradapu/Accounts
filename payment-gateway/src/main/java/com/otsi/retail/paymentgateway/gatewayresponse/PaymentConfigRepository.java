package com.otsi.retail.paymentgateway.gatewayresponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.paymentgateway.entity.PaymentConfig;
import com.otsi.retail.paymentgateway.vo.PaymentConfigVo;

@Repository
public interface PaymentConfigRepository extends JpaRepository<PaymentConfig, Long>{

	PaymentConfigVo save(PaymentConfigVo paymentConfigVoToEntity);
	
	
	

}
