package com.otsi.retail.paymentgateway.service;

public interface PaymentGatewayService {

    String createOrder(int amount) throws Exception;
}
