package com.maxtree.automotive.dashboard.domain;

/**
 * 档案袋
 * 
 * 一个档案袋对应一辆车的多笔业务
 * 
 * @author chens
 *
 */
public class Portfolio {

	public Integer getPortfolioUniqueId() {
		return portfolioUniqueId;
	}

	public void setPortfolioUniqueId(Integer portfolioUniqueId) {
		this.portfolioUniqueId = portfolioUniqueId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getFileBoxCode() {
		return fileBoxCode;
	}

	public void setFileBoxCode(String fileBoxCode) {
		this.fileBoxCode = fileBoxCode;
	}

	@Override
	public String toString() {
		return String.format("Portfolio[portfolioUniqueId=%d, vin='%s',serialNumber=%d,fileBoxCode='%s']",
				portfolioUniqueId, vin, serialNumber, fileBoxCode);
	}

	private Integer portfolioUniqueId;
	private String vin; // 车辆VIN
	private Integer serialNumber = 0;// 顺序号
	private String fileBoxCode;// 单元格CODE
}
