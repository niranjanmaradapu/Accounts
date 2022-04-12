package com.otsi.retail.paymentgateway.controller;

import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.paymentgateway.entity.PaymentConfig;
import com.otsi.retail.paymentgateway.gatewayresponse.GateWayResponse;
import com.otsi.retail.paymentgateway.service.PaymentConfigService;
import com.otsi.retail.paymentgateway.util.Constants;
import com.otsi.retail.paymentgateway.vo.PaymentConfigVo;


@RestController
@RequestMapping("/paymentconfig")
public class PaymentConfigController {

	private Logger log = LogManager.getLogger(PaymentConfigController.class);

	@Autowired
	private PaymentConfigService paymentConfigService;

/**
 * 	
 * @param paymentConfig 
 * Saving Data paymentconfig
 * @return
 */
	@PostMapping("/save")
	public GateWayResponse<?> save(@RequestBody PaymentConfigVo paymentConfigVo) {
		PaymentConfigVo savePaymentConfigDetails = paymentConfigService.save(paymentConfigVo);
		return new GateWayResponse<>(Constants.SUCCESS, savePaymentConfigDetails);
	}
	
	/**
	 * Retrieving All data
	 * @return
	 */
	
	@GetMapping("/list")
	public GateWayResponse<?> getpaymentConfig() {
		List<PaymentConfig> listofData = paymentConfigService.getListofData();
		return new GateWayResponse<>(Constants.SUCCESS, listofData);

	}
     
	/**
	 * Based on Id deleted data
	 * @param Id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public GateWayResponse<?> deleteById(@PathVariable("id") Long Id) {
		log.info("Recieved Delete request" +  Id);
		  paymentConfigService.deleteById(Id);
		return new GateWayResponse<>(Constants.SUCCESS);
	}

}
