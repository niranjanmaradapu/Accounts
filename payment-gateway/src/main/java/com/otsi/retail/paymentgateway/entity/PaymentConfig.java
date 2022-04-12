package com.otsi.retail.paymentgateway.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Rao
 * Adding new entity for paymentGateway 
 * *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_configuration")

public class PaymentConfig  extends BaseEntity{
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String secretKey;
	
	private String accessKey;
	
	private Boolean active;
	
	private String mobileNumber;
	
	
	

}
