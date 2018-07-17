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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getFileBoxSN() {
		return fileBoxSN;
	}

	public void setFileBoxSN(Integer fileBoxSN) {
		this.fileBoxSN = fileBoxSN;
	}
	
	public Integer getDenseframeSN() {
		return denseframeSN;
	}

	public void setDenseframeSN(Integer denseframeSN) {
		this.denseframeSN = denseframeSN;
	}

	public Integer getStorehouseSN() {
		return storehouseSN;
	}

	public void setStorehouseSN(Integer storehouseSN) {
		this.storehouseSN = storehouseSN;
	}

	@Override
	public String toString() {
		return String.format("Portfolio[portfolioUniqueId=%d, vin='%s', code='%s',fileBoxSN=%d,denseframeSN=%d,storehouseSN=%d]",
				portfolioUniqueId,vin,code,fileBoxSN,denseframeSN,storehouseSN);
	}

	private Integer portfolioUniqueId = 0;
	private String vin; // 车辆VIN
	private String code;// 上架号
	private Integer fileBoxSN = 0; //单元格顺序号（3位数）
	private Integer denseframeSN = 0;// 密集架顺序号
	private Integer storehouseSN = 0;// 库房顺序号
}
