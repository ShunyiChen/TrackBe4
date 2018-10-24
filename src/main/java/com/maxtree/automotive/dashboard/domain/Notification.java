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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public boolean isMarkedAsRead() {
		return markedAsRead;
	}

	public void setMarkedAsRead(boolean markedAsRead) {
		this.markedAsRead = markedAsRead;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderPicture() {
		return senderPicture;
	}

	public void setSenderPicture(String senderPicture) {
		this.senderPicture = senderPicture;
	}

	@Override
	public String toString() {
		return String.format(
				"Notification[notificationUniqueId=%d, messageUniqueId=%d, warning='%s' userUniqueId=%d,viewName='%s',relativeTime='%s',markedAsRead='%s',subject='%s',sender='%s',senderPicture='%s']",
				notificationUniqueId,messageUniqueId,warning,userUniqueId,viewName,sendTime,markedAsRead,subject,sender,senderPicture);
	}

	private Integer notificationUniqueId = 0; // 自增ID
	private Integer messageUniqueId = 0;// 消息ID
	private boolean warning;// 警告级别，true为警告，false为info
	private Integer userUniqueId;// 接收者ID
	private String viewName;// 视图名，区分相同接收者不同的视图
	private Date sendTime;// 发送时间
	private boolean markedAsRead;// 是否已读 ,true-是， false-否
	private String subject;// 消息标题（非数据库字段）
	private String sender;// 发送姓名 （非数据库字段）
	private String senderPicture;//发送者照片路径（非数据库字段）
}
