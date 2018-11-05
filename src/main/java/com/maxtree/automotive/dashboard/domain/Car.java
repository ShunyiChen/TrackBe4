package com.maxtree.automotive.dashboard.domain;

/**
 * 
 * @author Chen
 *
 */
public class Car {

	public Integer getCarUniqueId() {
		return carUniqueId;
	}

	public void setCarUniqueId(Integer carUniqueId) {
		this.carUniqueId = carUniqueId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPlateType() {
		return plateType;
	}

	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String toString() {
		return String.format(
				 "Car[carUniqueId=%d, barcode='%s', plateType='%s',plateNumber='%s',vin='%s']",
				 carUniqueId,
				 barcode,
				 plateType,
				 plateNumber,
				 vin
				);
	}
	
	private Integer carUniqueId = 0;
	private String barcode; // 条形码,即业务流水号
	private String plateType; // 号牌种类
	private String plateNumber; // 号码号牌
	private String vin; // 车辆识别代码
}
