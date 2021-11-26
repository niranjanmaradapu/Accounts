/*
 * controller for save
 */
package com.otsi.retail.paymentgateway.controller;

import java.util.List;
import java.util.Map;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		String newsaleOrderNumber = data.get("newsaleId").toString();// Adding new sale OrderId in request
		Order payment = paymentGatwayService.createOrder(amt, newsaleOrderNumber);
		return new GateWayResponse<String>("order created successfully", payment.toString());

	}

	@GetMapping("/fetchAllTranx")
	@ResponseBody
	public GateWayResponse<?> fetchAllTranx() throws Exception {
		log.info("Inside the create order service controller");
		List<Payment> payment = paymentGatwayService.fetchAllTranx();
		return new GateWayResponse<String>("fetched All transaction related to razorpay", payment.toString());

	}

	@GetMapping("/fetchTranxbyId")
	@ResponseBody
	public GateWayResponse<?> fetchTranxById(@RequestParam String razoyPayID) throws Exception {

		Order result = paymentGatwayService.fetchTranx(razoyPayID);
		return new GateWayResponse<>("fetched All transaction related to razorpay", result.toString());

	}
}