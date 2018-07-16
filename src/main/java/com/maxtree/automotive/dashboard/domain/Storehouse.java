package com.maxtree.automotive.dashboard.domain;

/**
 * 库房
 * 
 * 
 * 说明： 一个车管所可以包含多个库房;
 * 一个库房可以包含多个密集架;
 * 一个密集架包含多个单元格;
 * 一个单元格包含多个档案袋;
 * 一个档案袋包含一辆车的全部办理业务;
 * 
 * 
 * @author chens
 *
 */
public class Storehouse {
	public Integer getStorehouseUniqueId() {
		return storehouseUniqueId;
	}

	public void setStorehouseUniqueId(Integer storehouseUniqueId) {
		this.storehouseUniqueId = storehouseUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return String.format("Storehouse[storehouseUniqueId=%d, name='%s', code='%s']",
				storehouseUniqueId, name, code);
	}

	private Integer storehouseUniqueId = 0;
	private String name;//库房名
	private String code; //UUID,库房CODE
	private Integer companyUniqueId = 0; //机构ID
	private String companyName;//机构名称（非数据库字段）

}
