package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

public class UserEvent {

	public Integer getUserEventUniqueId() {
		return userEventUniqueId;
	}

	public void setUserEventUniqueId(Integer userEventUniqueId) {
		this.userEventUniqueId = userEventUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	private Integer userEventUniqueId = 0;//自增ID
	private String userName; // 操作者用户名
	private String action;// 事件动作
	private String details;// 详细信息json
	private Date dateUpdated;// 更新日期
}
