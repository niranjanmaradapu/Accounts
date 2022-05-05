package com.otsi.retail.hsnDetails.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

	@Value("${getStoreDetails_url}")
	private String storeDetails;
	
	@Value("${getUserDetails_url}")
	private String userDetails;
	
	@Value("${getCustomerDetailsFromURM_url}")
	private String getCustomerDetailsFromURM;


}