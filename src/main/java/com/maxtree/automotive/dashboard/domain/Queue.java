package com.maxtree.automotive.dashboard.domain;

public class Queue {

	public Integer getQueueUniqueId() {
		return queueUniqueId;
	}

	public void setQueueUniqueId(Integer queueUniqueId) {
		this.queueUniqueId = queueUniqueId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getLockedByUser() {
		return lockedByUser;
	}

	public void setLockedByUser(Integer lockedByUser) {
		this.lockedByUser = lockedByUser;
	}

	public Integer getCompanyUniqueId() {
		return companyUniqueId;
	}

	public void setCompanyUniqueId(Integer companyUniqueId) {
		this.companyUniqueId = companyUniqueId;
	}

	public Integer getCommunityUniqueId() {
		return communityUniqueId;
	}

	public void setCommunityUniqueId(Integer communityUniqueId) {
		this.communityUniqueId = communityUniqueId;
	}

	@Override
	public String toString() {
		return String.format(
				"Queue[queueUniqueid=%d, uuid='%s',vin='%s',lockedByUser='%d',sentByUser='%d',companyUniqueId='%d',communityUniqueId='%d']",
				queueUniqueId, uuid, vin, lockedByUser, companyUniqueId, communityUniqueId);
	}

	private Integer queueUniqueId = 0; // 队列ID
	private String uuid;// 文件挂载ID
	private String vin; // VIN
	private Integer lockedByUser = 0; // 锁定记录的用户ID，0-没锁，1-锁了
	private Integer companyUniqueId = 0; // 机构ID 如果companyUniqueId为空，表示本社区内任何审档都可以取，否则只允许本机审档构取。
	private Integer communityUniqueId = 0; // 社区ID 如果communityUniqueId，本社区内的审档/质检只能取本社区队列。
}
