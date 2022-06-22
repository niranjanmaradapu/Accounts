package com.otsi.retail.paymentgateway.service;

import com.otsi.retail.paymentgateway.vo.AccountVo;
import com.otsi.retail.paymentgateway.vo.PaymentDetailsVo;
import com.razorpay.Order;
import com.razorpay.Payment;

import java.util.List;

public interface PaymentGatewayService {

	Order createOrder(int amount, String newsaleOrderNumber) throws Exception;

	List<Payment> fetchAllTranx() throws Exception;

	Order fetchTranx(String razoyPayID) throws Exception;

	PaymentDetailsVo createCreditDebitOrder(AccountVo accountVo) throws Exception;



}
