package com.otsi.retail.paymentgateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    @Value("${RazoryPay.key_Id}")
    private String Key;
    @Value("${RazorPay.key_Secret}")
    private String secert;

    @Value("${paymenyUpdateNewsale}")
    private String restCallForNewsalePayments;
}
