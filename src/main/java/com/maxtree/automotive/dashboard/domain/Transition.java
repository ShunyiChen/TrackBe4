package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 跟踪业务记录
 * 
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

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	private Integer transitionUniqueId = 0;// 自增ID
	private String transactionUUID;// 记录ID
	private String vin;//车辆识别代号
	private String activity;// 事件动作
	private String comments;// 评论
	private String operator;// 操作者用户名
	private Date dateCreated;// 创建日期
}
