package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 跟踪业务记录
 * @author chens
 *
 */
public class Transition {

	public Integer getTransitionUniqueId() {
		return transitionUniqueId;
	}

	public void setTransitionUniqueId(Integer transitionUniqueId) {
		this.transitionUniqueId = transitionUniqueId;
	}

	public String getTransactionUUID() {
		return transactionUUID;
	}

	public void setTransactionUUID(String transactionUUID) {
		this.transactionUUID = transactionUUID;
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
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	private Integer transitionUniqueId = 0;// 自增ID
	private String transactionUUID;// 记录ID
	private String action;// 事件动作
	private String details;//详细信息json
	private String comments;//评论
	private String userName;//操作者用户名
	private Date dateUpdated;// 更新日期
}
