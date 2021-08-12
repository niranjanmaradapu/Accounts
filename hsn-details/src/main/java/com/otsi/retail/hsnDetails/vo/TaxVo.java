package com.otsi.retail.hsnDetails.vo;

/*
 * vo for Tax
*/
import lombok.Data;

/**
 * @author vasavi
 *
 */

@Data
public class TaxVo {

	private long id;
	private String taxLabel;
	private float sgst;
	private float cgst;
	private float igst;
	private float cess;

}
