package com.maxtree.automotive.dashboard.domain;

public class MessageRecipient {

	public Integer getMessageRecipientUniqueId() {
		return messageRecipientUniqueId;
	}

	public void setMessageRecipientUniqueId(Integer messageRecipientUniqueId) {
		this.messageRecipientUniqueId = messageRecipientUniqueId;
	}

	public Integer getRecipientUniqueId() {
		return recipientUniqueId;
	}

	public void setRecipientUniqueId(Integer recipientUniqueId) {
		this.recipientUniqueId = recipientUniqueId;
	}
	
	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public Integer getRecipientType() {
		return recipientType;
	}

	public void setRecipientType(Integer recipientType) {
		this.recipientType = recipientType;
	}

	public Integer getMessageUniqueId() {
		return messageUniqueId;
	}

	public void setMessageUniqueId(Integer messageUniqueId) {
		this.messageUniqueId = messageUniqueId;
	}
	
	@Override
	public String toString() {
		 return String.format("MessageRecipient[recipientUniqueId=%d, recipientName='%s', recipientType=%d, messageUniqueId=%d]",
				 recipientUniqueId, recipientName, recipientType, messageUniqueId);
	}

	private Integer messageRecipientUniqueId = 0;
	private Integer recipientUniqueId = 0; // 接收方ID
	private String recipientName;  		   // 接收方名称
	private Integer recipientType = 0;	   // 接收方类别，1-社区，2-机构，3-用户
	private Integer messageUniqueId = 0;   // 消息ID
}
