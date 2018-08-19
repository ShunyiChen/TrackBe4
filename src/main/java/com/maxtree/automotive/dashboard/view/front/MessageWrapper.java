package com.maxtree.automotive.dashboard.view.front;

import java.util.Date;

public class MessageWrapper {

	/**
	 * 
	 * @param messageUniqueId
	 * @param senderUserName
	 * @param senderPicture
	 * @param subject
	 * @param message
	 * @param uuid
	 * @param read
	 * @param dateCreated
	 * @param type
	 */
	public MessageWrapper(int messageUniqueId, String senderUserName, String senderPicture, String subject,
			String message, String uuid, String read, Date dateCreated, String type, String status) {
		this.messageUniqueId = messageUniqueId;
		this.senderUserName = senderUserName;
		this.senderPicture = senderPicture;
		this.subject = subject;
		this.message = message;
		this.read = read;
		this.dateCreated = dateCreated;
		
		// transaction information
		this.uuid = uuid;
		this.type = type;
		this.status = status;
	}

	public int getMessageUniqueId() {
		return messageUniqueId;
	}

	public void setMessageUniqueId(int messageUniqueId) {
		this.messageUniqueId = messageUniqueId;
	}

	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getSenderPicture() {
		return senderPicture;
	}

	public void setSenderPicture(String senderPicture) {
		this.senderPicture = senderPicture;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private int messageUniqueId;
	private String uuid;
	private String senderUserName;
	private String senderPicture;
	private String subject;
	private String message;
	private String read;
	private Date dateCreated;
	private String type; // 消息类别
	private String status;
}
