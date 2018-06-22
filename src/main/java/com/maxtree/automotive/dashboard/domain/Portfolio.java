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

	public Integer getFileBoxUniqueId() {
		return fileBoxUniqueId;
	}

	public void setFileBoxUniqueId(Integer fileBoxUniqueId) {
		this.fileBoxUniqueId = fileBoxUniqueId;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Override
	public String toString() {
		return String.format("Portfolio[portfolioUniqueId=%d, fileBoxUniqueId=%d, code='%s', num=%d, vin=%s]", portfolioUniqueId, fileBoxUniqueId, code, vin);
	}
	
	private Integer portfolioUniqueId;  
	private Integer fileBoxUniqueId;
	private String code;		
	private String vin; 	// 车辆VIN
}
