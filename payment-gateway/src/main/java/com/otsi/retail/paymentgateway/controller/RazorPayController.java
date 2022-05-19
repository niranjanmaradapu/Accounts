package com.otsi.retail.paymentgateway.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.paymentgateway.gatewayresponse.GateWayResponse;
import com.otsi.retail.paymentgateway.service.RazorPayImpl;
import com.otsi.retail.paymentgateway.util.EndpointConstants;
import com.otsi.retail.paymentgateway.vo.PaymentLinkRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Saikiran Kola
 *
 */
@RestController
@RequestMapping("/razorpay")
@Slf4j
public class RazorPayController {

	private Logger log = LogManager.getLogger(RazorPayController.class);

	@Autowired
	private RazorPayImpl razorPayImpl;

	/**
	 * 
	 * @param paymentLinkRequest
	 * @return
	 */
	@PostMapping("/create-payment-link")
	public GateWayResponse<?> fetchAllTranx(@RequestBody PaymentLinkRequest paymentLinkRequest) {
		Map<String, Object> payment = razorPayImpl.createPaymentLink(paymentLinkRequest);
		return new GateWayResponse<>(HttpStatus.OK, payment);
	}

	/**
	 * 
	 * @param transaction
	 * @return
	 */
	@GetMapping(EndpointConstants.RAZORPAY_CALLBACK_URL)
	public GateWayResponse<?> fetchAllTranx(@RequestParam Map<String, Object> transaction) {
		log.info("Razorpay callback received after transaction {} ", transaction);
		return new GateWayResponse<>(HttpStatus.ACCEPTED, transaction);
	}

}
