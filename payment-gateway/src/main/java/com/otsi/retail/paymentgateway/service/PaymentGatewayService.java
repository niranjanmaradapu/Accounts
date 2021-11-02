package com.otsi.retail.paymentgateway.service;

import com.razorpay.Order;
import com.razorpay.Payment;

import java.util.List;

public interface PaymentGatewayService {

    Order createOrder(int amount) throws Exception;

    List<Payment> fetchAllTranx() throws Exception;
}
