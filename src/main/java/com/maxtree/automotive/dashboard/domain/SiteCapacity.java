package com.maxtree.automotive.dashboard.domain;

public class SiteCapacity {

	public Integer getSiteCapacityUniqueId() {
		return siteCapacityUniqueId;
	}

	public void setSiteCapacityUniqueId(Integer siteCapacityUniqueId) {
		this.siteCapacityUniqueId = siteCapacityUniqueId;
	}

	public Integer getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(Integer siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public Long getUsedSpace() {
		return usedSpace;
	}

	public void setUsedSpace(Long usedSpace) {
		this.usedSpace = usedSpace;
	}

	public Long getUpdateTimeMillis() {
		return updateTimeMillis;
	}

	public void setUpdateTimeMillis(Long updateTimeMillis) {
		this.updateTimeMillis = updateTimeMillis;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	
	public Integer getMaxBatch() {
		return maxBatch;
	}

	public void setMaxBatch(Integer maxBatch) {
		this.maxBatch = maxBatch;
	}

	public Integer getMaxBusiness() {
		return maxBusiness;
	}

	public void setMaxBusiness(Integer maxBusiness) {
		this.maxBusiness = maxBusiness;
	}

	private Integer siteCapacityUniqueId = 0;
	private Integer siteUniqueId = 0;
	private Long capacity = 0L;
	private Long usedSpace = 0L;
	private Long updateTimeMillis = 0L;
	private String unit;
	private String unitNumber;
	private Integer maxBatch = 0; // 最大批次数
	private Integer maxBusiness = 0; // 最大业务数
}
