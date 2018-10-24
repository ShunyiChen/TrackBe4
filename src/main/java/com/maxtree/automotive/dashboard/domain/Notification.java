package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 
 * @author Chen
 *
 */
public class Notification {
	
	public Integer getNotificationUniqueId() {
		return notificationUniqueId;
	}
	public void setNotificationUniqueId(Integer notificationUniqueId) {
		this.notificationUniqueId = notificationUniqueId;
	}
	public Integer getMessageUniqueId() {
		return messageUniqueId;
	}
	public void setMessageUniqueId(Integer messageUniqueId) {
		this.messageUniqueId = messageUniqueId;
	}
	public boolean isWarning() {
		return warning;
	}
	public void setWarning(boolean warning) {
		this.warning = warning;
	}
	public Integer getUserUniqueId() {
		return userUniqueId;
	}
	public void setUserUniqueId(Integer userUniqueId) {
		this.userUniqueId = userUniqueId;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public Date getRelativeTime() {
		return relativeTime;
	}
	public void setRelativeTime(Date relativeTime) {
		this.relativeTime = relativeTime;
	}
	public boolean isMarkedAsRead() {
		return markedAsRead;
	}
	public void setMarkedAsRead(boolean markedAsRead) {
		this.markedAsRead = markedAsRead;
	}
	@Override
	public String toString() {
		return String.format(
				"Notification[notificationUniqueId=%d, messageUniqueId=%d, warning='%s' userUniqueId=%d,viewName='%s',relativeTime='%s',markedAsRead='%s']",
				notificationUniqueId, messageUniqueId,warning, userUniqueId, viewName,relativeTime,markedAsRead);
	}
	
	private Integer notificationUniqueId = 0; //自增ID
	private Integer messageUniqueId = 0;//消息ID
	private boolean warning;//警告级别，true为警告，false为info
	private Integer userUniqueId;//接收者ID
	private String viewName;//视图名，区分相同接收者不同的视图
	private Date relativeTime;//相对时间
	private boolean markedAsRead;//是否已读 ,true-是， false-否
	
}
