package com.otsi.retail.paymentgateway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.otsi.retail.paymentgateway.gatewayresponse.GateWayResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.paymentgateway.config.Config;
import com.otsi.retail.paymentgateway.vo.PaymentDetailsVo;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import org.springframework.web.client.RestTemplate;

@Component
public class RazorPayImpl implements PaymentGatewayService {

	private Logger log = LoggerFactory.getLogger(RazorPayImpl.class);

	@Autowired
	Config config;

	@Autowired
	private RestTemplate template;

	@Override
	// @SuppressWarnings("unchecked")
	public Order createOrder(int amount, String newsaleOrderNumber) throws Exception {
		log.debug("debugging createOrder():" + amount);
		log.info("Inside the create method of service class");
		RazorpayClient client = null;

		client = new RazorpayClient(config.getKey(), config.getSecert());
		JSONObject ob = new JSONObject();
		ob.put("amount", amount * 100);
		ob.put("currency", "INR");
		ob.put("receipt", newsaleOrderNumber);

		// creating new order

		Order order = client.Orders.create(ob);
		System.out.println(order);

		// KLModerid ,razor pay id-payment table rest call to new sale...
		List<PaymentDetailsVo> listVo = new ArrayList<>();
		
		PaymentDetailsVo vo = new PaymentDetailsVo();
		vo.setNewsaleOrder(newsaleOrderNumber);
		vo.setAmount(Long.valueOf(amount));
		listVo.add(vo);
		// HashMap<String,Object> result =
		// new ObjectMapper().readValues(order,HashMap.class);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<String>> request = new HttpEntity(listVo, headers);

		ResponseEntity<GateWayResponse> response = template.exchange(config.getRestCallForNewsalePayments(),
				HttpMethod.POST, request, GateWayResponse.class);

		String message = response.getBody().getMessage();

		log.info("order created succesfully:" + order.toString());
		return order;
	}

	@Override
	public List<Payment> fetchAllTranx() throws Exception {
		RazorpayClient client = null;
		client = new RazorpayClient(config.getKey(), config.getSecert());
		List<Payment> payments = client.Payments.fetchAll();
		log.info("All payments:" + payments.toString());
		return payments;
	}
}
