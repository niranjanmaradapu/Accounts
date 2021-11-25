package com.otsi.retail.paymentgateway.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.paymentgateway.config.Config;
import com.otsi.retail.paymentgateway.vo.PaymentDetailsVo;
import com.otsi.retail.paymentgateway.vo.RazorPayResponse;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

@Component
public class RazorPayImpl implements PaymentGatewayService {

	private Logger log = LoggerFactory.getLogger(RazorPayImpl.class);

	@Autowired
	Config config;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
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
		log.info("order created succesfully:" + order.toString());

		ObjectMapper objectMapper = new ObjectMapper();
		RazorPayResponse razorPayResponse = objectMapper.readValue(order.toString(), RazorPayResponse.class);
		PaymentDetailsVo vo = new PaymentDetailsVo();

		vo.setNewsaleOrder(newsaleOrderNumber);
		vo.setAmount(Long.valueOf(amount));
		vo.setPayType("Card");
		vo.setRazorPayId(razorPayResponse.getId());

		// Pass object to Rabbit queue
		rabbitTemplate.convertAndSend(config.getNewSaleExchange(), config.getPaymentNewsaleRK(), vo);
		log.info("order details queue send to newsale " + order.toString());

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

	@Override
	public Order fetchTranx(String razorPayID) throws Exception {
		RazorpayClient client = null;
		client = new RazorpayClient(config.getKey(), config.getSecert());
		Order order = client.Orders.fetch(razorPayID.trim());
		return order;
	}
}
