package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

public class Message {

	public Integer getMessageUniqueId() {
		return messageUniqueId;
	}

	public void setMessageUniqueId(Integer messageUniqueId) {
		this.messageUniqueId = messageUniqueId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public Integer getCreatorUniqueId() {
		return creatorUniqueId;
	}

	public void setCreatorUniqueId(Integer creatorUniqueId) {
		this.creatorUniqueId = creatorUniqueId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getSentTimes() {
		return sentTimes;
	}

	public void setSentTimes(Integer sentTimes) {
		this.sentTimes = sentTimes;
	}
	
	public Integer getReminderFrequencyId() {
		return reminderFrequencyId;
	}

	public void setReminderFrequencyId(Integer reminderFrequencyId) {
		this.reminderFrequencyId = reminderFrequencyId;
	}

	public Integer getReadRate() {
		return readRate;
	}

	public void setReadRate(Integer readRate) {
		this.readRate = readRate;
	}

	@Override
	public String toString() {
		 return String.format("Message[messageUniqueId=%d, subject='%s', messageBody='%s']",
				 messageUniqueId, subject, messageBody);
	}

	private Integer messageUniqueId = 0;
	private String subject;// 标题
	private String messageBody;// 消息体json
	private Integer creatorUniqueId = 0; // 创建者ID
	private Date dateCreated; // 创建日期
	private Integer sentTimes = 0; // 已发送次数
	private Integer reminderFrequencyId = 0; // 发送频率ID
	private Integer readRate = 0; // 读取率
}
