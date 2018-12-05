package com.maxtree.automotive.dashboard.domain;

/**
 * 待终审记录表
 * 
 * @author Chen
 *
 */
public class FinalCheck {

	public Integer getCheckUniqueId() {
		return checkUniqueId;
	}
	public void setCheckUniqueId(Integer checkUniqueId) {
		this.checkUniqueId = checkUniqueId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	private Integer checkUniqueId = 0;
	private String barcode;
	private String vin;
}
