/*
 * controller for save,update,delete and getEnums
 */
package com.otsi.retail.paymentgateway.controller;

import java.util.Map;

import com.otsi.retail.paymentgateway.service.PaymentGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
    public String createOrder(@RequestBody Map<String, Object> data) throws Exception
    {
        log.info("Inside the create order service controller");
        System.out.println(data);
        int amt=Integer.parseInt(data.get("amount").toString());
        return paymentGatwayService.createOrder(amt);
    }


}