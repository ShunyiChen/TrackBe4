package com.maxtree.automotive.dashboard.domain;

/**
 * 站点文件夹数
 * 
 * @author chens
 *
 */
public class SiteFolder {

	public Integer getSiteFolderUniqueId() {
		return siteFolderUniqueId;
	}

	public void setSiteFolderUniqueId(Integer siteFolderUniqueId) {
		this.siteFolderUniqueId = siteFolderUniqueId;
	}

	public Integer getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(Integer siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
	}

	public Integer getBatchCount() {
		return batchCount;
	}

	public void setBatchCount(Integer batchCount) {
		this.batchCount = batchCount;
	}

	public Integer getBusinessCount() {
		return businessCount;
	}

	public void setBusinessCount(Integer businessCount) {
		this.businessCount = businessCount;
	}

	private Integer siteFolderUniqueId = 0; // 自增长ID
	private Integer siteUniqueId = 0;		// 站点ID
	private Integer batchCount = 0; 		// 批次增长数
	private Integer businessCount = 0; 		// 批次内业务增长数量
}
