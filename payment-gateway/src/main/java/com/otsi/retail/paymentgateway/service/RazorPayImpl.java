package com.otsi.retail.paymentgateway.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.paymentgateway.config.Config;
import com.otsi.retail.paymentgateway.util.EndpointConstants;
import com.otsi.retail.paymentgateway.vo.AccountVo;
import com.otsi.retail.paymentgateway.vo.PaymentDetailsVo;
import com.otsi.retail.paymentgateway.vo.PaymentLinkRequest;
import com.otsi.retail.paymentgateway.vo.RazorPayResponse;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Component
public class RazorPayImpl implements PaymentGatewayService {

	private Logger log = LogManager.getLogger(RazorPayImpl.class);

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

		vo.setReferenceNumber(newsaleOrderNumber);
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

	/**
	 * 
	 * @param paymentLinkRequest
	 * @return
	 */
	public Map<String, Object> createPaymentLink(PaymentLinkRequest paymentLinkRequest) {
		ObjectMapper objMapper = new ObjectMapper();
		String customerDetails = null;
		try {
			customerDetails = objMapper.writeValueAsString(paymentLinkRequest.getCustomerDetails());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		String referenceId = UUID.randomUUID().toString();
		String url = "https://api.razorpay.com/v1/payment_links";
		String callbackUrl = config.getRazorpayCallBackUrl() + EndpointConstants.RAZORPAY_CALLBACK_URL;

		String body = "{\r\n" + "    \"amount\": " + paymentLinkRequest.getAmount() + "," + "\r\n"
				+ "    \"currency\": \"INR\",\r\n" + "    \"accept_partial\": true," + "\r\n"
				+ "    \"first_min_partial_amount\": 0,\r\n" + "    \"expire_by\": 1691097057,\r\n"
				+ "    \"reference_id\":  \"" + referenceId + "\",\r\n" + "    \"description\": \""
				+ paymentLinkRequest.getDescription() + "\",\r\n" + "    \"customer\": " + customerDetails + ",\r\n"
				+ "    \"notify\": {\r\n" + "        \"sms\": true,\r\n" + "        \"email\": true\r\n" + "    },\r\n"
				+ "    \"reminder_enable\": true,\r\n" + "    \"notes\": {\r\n"
				+ "        \"policy_name\": \"Jeevan Bima\"\r\n" + "    },\r\n" + "    \"callback_url\": \""
				+ callbackUrl + "\",\r\n" + "    \"callback_method\": \"get\"\r\n" + "}";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(config.getKey(), config.getSecert());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		try {
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
			ResponseEntity<Map> response = restTemplate.postForEntity(uriBuilder.toUriString(), request, Map.class);
			if (response != null && response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				return (Map<String, Object>) response.getBody();
			}
		} catch (Exception e) {
			System.out.println("Exception while invoking payment create link :" + e.getMessage());
		}
		return null;
	}
	@Override
	public PaymentDetailsVo createCreditDebitOrder(AccountVo accountVo) throws Exception {
		
		
		
		
		RazorPayResponse razorPayResponse =	 getPayment(accountVo.getAmount(),accountVo.getReferenceNumber());
		PaymentDetailsVo vo = new PaymentDetailsVo();

		vo.setReferenceNumber(accountVo.getReferenceNumber());
		vo.setAmount(Long.valueOf(accountVo.getAmount()));
		vo.setPayType("Card");
		vo.setRazorPayId(razorPayResponse.getId());
		

		// Pass object to Rabbit queue
		rabbitTemplate.convertAndSend(config.getPayemntsExchange(), config.getPaymentCreditNotesRK(), vo);
		///log.info("order details queue send to newsale " + order.toString());

		return vo;

		

		
	}


	private RazorPayResponse getPayment(int amt, String orderNumber) throws RazorpayException, JsonMappingException, JsonProcessingException {
		RazorpayClient client = new RazorpayClient(config.getKey(), config.getSecert());
		JSONObject ob = new JSONObject();
		ob.put("amount", amt * 100);
		ob.put("currency", "INR");
		ob.put("receipt", orderNumber);

		// creating new order
	
		Order order = client.Orders.create(ob);
		log.info("order created succesfully:" + order.toString());
		ObjectMapper objectMapper = new ObjectMapper();
	return	objectMapper.readValue(order.toString(), RazorPayResponse.class);
		
	}
}
