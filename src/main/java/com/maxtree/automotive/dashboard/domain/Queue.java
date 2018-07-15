package com.maxtree.automotive.dashboard.domain;

public class Queue {

	public Integer getQueueUniqueId() {
		return queueUniqueId;
	}

	public void setQueueUniqueId(Integer queueUniqueId) {
		this.queueUniqueId = queueUniqueId;
	}

	public Integer getTransactionUniqueId() {
		return transactionUniqueId;
	}

	public void setTransactionUniqueId(Integer transactionUniqueId) {
		this.transactionUniqueId = transactionUniqueId;
	}

	public Integer getLockedByUser() {
		return lockedByUser;
	}

	public void setLockedByUser(Integer lockedByUser) {
		this.lockedByUser = lockedByUser;
	}

	public Integer getSentByUser() {
		return sentByUser;
	}

	public void setSentByUser(Integer sentByUser) {
		this.sentByUser = sentByUser;
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
		return String.format("Queue[queueUniqueid=%d, transactionUniqueId='%d',lockedByUser='%d',sentByUser='%d',companyUniqueId='%d',communityUniqueId='%d']",
				queueUniqueId, transactionUniqueId, lockedByUser, sentByUser,companyUniqueId,communityUniqueId);
	}

	private Integer queueUniqueId = 0;       // 队列ID
	private Integer transactionUniqueId = 0; // 事务ID
	private Integer lockedByUser = 0; 		 // 锁定记录的用户ID，0-没锁，1-锁了
	private Integer sentByUser = 0; 		 // 发送者
	private Integer companyUniqueId = 0;	 // 机构ID 如果companyUniqueId为空，表示本社区内任何审档都可以取，否则只允许本机审档构取。
	private Integer communityUniqueId = 0;   // 社区ID 如果communityUniqueId，本社区内的审档/质检只能取本社区队列。
}
