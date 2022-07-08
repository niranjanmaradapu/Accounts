/*
 * enum for taxAppliesOn field
*/

package com.otsi.retail.hsnDetails.enums;

/**
 * @author vasavi
 *
 */
public enum TaxAppliesOn {
	//LISTEDPRICE(1, "listedPrice"),
	 MRP(2, "mrp");

	private int id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	private TaxAppliesOn(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
