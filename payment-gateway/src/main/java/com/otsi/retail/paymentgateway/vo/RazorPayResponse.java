package com.otsi.retail.paymentgateway.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RazorPayResponse {
	
	public int amount;
    public int amount_paid;
    public List<Object> notes;
    public int created_at;
    public int amount_due;
    public String currency;
    public String receipt;
    public String id;
    public String entity;
    public Object offer_id;
    public String status;
    public int attempts; 

}
