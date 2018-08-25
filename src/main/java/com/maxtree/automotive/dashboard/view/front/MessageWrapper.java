package com.maxtree.automotive.dashboard.view.front;

import java.util.Date;

public class MessageWrapper {

	/**
	 * 
	 * @param messageUniqueId
	 * @param senderUserName
	 * @param senderPicture
	 * @param subject
	 * @param content
	 * @param matedata
	 * @param read
	 * @param dateCreated
	 */
	public MessageWrapper(int messageUniqueId, String senderUserName, String senderPicture, String subject,
			String content,String matedata, String read, Date dateCreated) {
		this.messageUniqueId = messageUniqueId;
		this.senderUserName = senderUserName;
		this.senderPicture = senderPicture;
		this.subject = subject;
		this.content = content;
		this.matedata = matedata;
		this.read = read;
		this.dateCreated = dateCreated;
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
	
	private int messageUniqueId;
	private String senderUserName;
	private String senderPicture;
	private String subject;
	private String content;
	private String matedata;
	private String read;
	private Date dateCreated;
	
}
