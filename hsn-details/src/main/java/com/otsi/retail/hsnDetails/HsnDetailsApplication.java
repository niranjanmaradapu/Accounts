package com.otsi.retail.hsnDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HsnDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsnDetailsApplication.class, args);
	}

}
