package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 消息
 * 
 * @author chens
 *
 */
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMatedata() {
		return matedata;
	}
	public void setMatedata(String matedata) {
		this.matedata = matedata;
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
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Integer getReadRate() {
		return readRate;
	}
	public void setReadRate(Integer readRate) {
		this.readRate = readRate;
	}
	@Override
	public String toString() {
		 return String.format("Message[messageUniqueId=%d, subject='%s', content='%s',matedata='%s',creatorUniqueId=%d,dateCreated='%s',sentTimes=%d,reminderFrequencyId=%d,deleted=%d,readRate=%d]",
				 messageUniqueId,subject,content,matedata,creatorUniqueId,dateCreated.toString(),sentTimes,reminderFrequencyId,deleted,readRate);
	}

	private Integer messageUniqueId = 0;
	private String subject;// 标题
	private String content;// 消息内容
	private String matedata;//元数据，属性(openwith, transitionUniqueId)
	private Integer creatorUniqueId = 0; // 创建者ID
	private Date dateCreated; // 创建日期
	private Integer sentTimes = 0; // 已发送次数
	private Integer reminderFrequencyId = 0; // 发送频率ID
	private Integer deleted = 0; // 删除标识，1-删除 0-未删除(如果群发邮件，管理员删除邮件其他接收者将看不到内容，所以用delete标识)
	private Integer readRate = 0; // 读取率(非数据库字段)
}
