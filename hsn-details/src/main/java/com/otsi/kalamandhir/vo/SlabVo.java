/*
 * vo for Slab
*/
package com.otsi.kalamandhir.vo;

import lombok.Data;

/**
 * @author vasavi
 *
 */
@Data
public class SlabVo {

	private long id;
	private double priceFrom;
	private double priceTo;
	private TaxVo taxVo;

}
