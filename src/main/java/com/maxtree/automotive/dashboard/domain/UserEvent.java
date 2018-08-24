package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 跟踪用户记录
 * @author chens
 *
 */
public class UserEvent {

	public Integer getUserEventUniqueId() {
		return userEventUniqueId;
	}

	public void setUserEventUniqueId(Integer userEventUniqueId) {
		this.userEventUniqueId = userEventUniqueId;
	}

	public Integer getTransitionUniqueId() {
		return transitionUniqueId;
	}

	public void setTransitionUniqueId(Integer transitionUniqueId) {
		this.transitionUniqueId = transitionUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String toString() {
		return String.format("UserEvent[userEventUniqueId=%d, transitionUniqueId=%d, userName='%s',dateUpdated='%s']",
				userEventUniqueId, transitionUniqueId, userName, dateUpdated.toString());
	}

	private Integer userEventUniqueId = 0;// 自增ID
	private Integer transitionUniqueId = 0;// 迁移ID
	private String userName; // 操作者用户名
	private Date dateUpdated;// 更新日期
}
