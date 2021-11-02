/*
 * controller for save
 */
package com.otsi.retail.paymentgateway.controller;

import java.util.List;
import java.util.Map;

import com.razorpay.Order;
import com.razorpay.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.otsi.retail.paymentgateway.gatewayresponse.GateWayResponse;
import com.otsi.retail.paymentgateway.service.PaymentGatewayService;

/**
 * @author vasavi
 *
 */
@Controller
@RequestMapping("/paymentgateway")
public class PaymentGatewayController {

	private Logger log = LoggerFactory.getLogger(PaymentGatewayController.class);

	@Autowired
	private PaymentGatewayService paymentGatwayService;

	@PostMapping("/create_order")
	@ResponseBody
	public GateWayResponse<?> createOrder(@RequestBody Map<String, Object> data) throws Exception {
		log.info("Inside the create order service controller");
		System.out.println(data);
		int amt = Integer.parseInt(data.get("amount").toString());
		Order payment=paymentGatwayService.createOrder(amt);
		return new GateWayResponse<String>("order created successfully",payment.toString());

	}

	@PostMapping("/fetchAllTranx")
	@ResponseBody
	public GateWayResponse<?> fetchAllTranx(@RequestBody Map<String, Object> data) throws Exception {
		log.info("Inside the create order service controller");
		System.out.println(data);
		int amt = Integer.parseInt(data.get("amount").toString());
		List<Payment> payment=paymentGatwayService.fetchAllTranx();
		return new GateWayResponse<String>("fetched All transaction related to razorpay",payment.toString());

	}

}