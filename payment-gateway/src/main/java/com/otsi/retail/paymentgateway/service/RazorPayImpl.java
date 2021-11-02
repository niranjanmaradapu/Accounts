package com.otsi.retail.paymentgateway.service;

import com.otsi.retail.paymentgateway.config.Config;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RazorPayImpl implements PaymentGatewayService {

	private Logger log = LoggerFactory.getLogger(RazorPayImpl.class);

	@Autowired
	Config config;

	@Override
	public Order createOrder(int amount) throws Exception {
		log.debug("debugging createOrder():" + amount);
		log.info("Inside the create method of service class");
		RazorpayClient client = null;

		client = new RazorpayClient(config.getKey(), config.getSecert());
		JSONObject ob = new JSONObject();
		ob.put("amount", amount * 100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");

		// creating new order

		Order order = client.Orders.create(ob);
		System.out.println(order);
		log.info("order created succesfully:" + order.toString());
		return order;
	}

	@Override
	public List<Payment> fetchAllTranx() throws Exception{
		RazorpayClient client = null;
		client = new RazorpayClient(config.getKey(), config.getSecert());
		List<Payment> payments = client.Payments.fetchAll();
		log.info("All payments:" + payments.toString());
		return payments;
	}
}
