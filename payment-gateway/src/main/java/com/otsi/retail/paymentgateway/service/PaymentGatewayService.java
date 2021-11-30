package com.otsi.retail.paymentgateway.service;

import com.razorpay.Order;
import com.razorpay.Payment;

import java.util.List;

public interface PaymentGatewayService {

	Order createOrder(int amount, String newsaleOrderNumber) throws Exception;

	List<Payment> fetchAllTranx() throws Exception;

	Order fetchTranx(String razoyPayID) throws Exception;
}
