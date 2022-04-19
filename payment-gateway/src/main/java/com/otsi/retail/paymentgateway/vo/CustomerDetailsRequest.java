package com.otsi.retail.paymentgateway.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class CustomerDetailsRequest {

	private String name;

	private String contact;

	private String email;
}
