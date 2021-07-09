/*
 * enum for taxAppliesOn field
*/

package com.otsi.kalamandhir.enums;

/**
 * @author vasavi
 *
 */
public enum TaxAppliesOn {

	NETPRICE(1, "netprice"), RSP(2, "rsp");

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
