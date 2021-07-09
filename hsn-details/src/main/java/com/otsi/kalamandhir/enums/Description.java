/*
 * enum for description field
*/
package com.otsi.kalamandhir.enums;

/**
 * @author vasavi
 *
 */
public enum Description {
	GOODS(1, "Goods"), SERVICES(2, "Services");

	private int id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	private Description(int id, String name) {
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
