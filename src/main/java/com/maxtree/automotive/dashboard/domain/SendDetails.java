package com.maxtree.automotive.dashboard.domain;

/**
 * 发送消息记录
 * 
 * @author chens
 *
 */
public class SendDetails {

	public Integer getSendDetailsUniqueId() {
		return sendDetailsUniqueId;
	}

	public void setSendDetailsUniqueId(Integer sendDetailsUniqueId) {
		this.sendDetailsUniqueId = sendDetailsUniqueId;
	}

	public Integer getRecipientUniqueId() {
		return recipientUniqueId;
	}

	public void setRecipientUniqueId(Integer recipientUniqueId) {
		this.recipientUniqueId = recipientUniqueId;
	}

	public Integer getMessageUniqueId() {
		return messageUniqueId;
	}

	public void setMessageUniqueId(Integer messageUniqueId) {
		this.messageUniqueId = messageUniqueId;
	}

	public Integer getMarkedAsRead() {
		return markedAsRead;
	}

	public void setMarkedAsRead(Integer markedAsRead) {
		this.markedAsRead = markedAsRead;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public String toString() {
		return String.format("SendDetails[recipientUniqueId=%d, messageUniqueId=%d, markedAsRead=%d, viewName='%s']",
				recipientUniqueId, messageUniqueId, markedAsRead, viewName);
	}

	private Integer sendDetailsUniqueId = 0;
	private Integer recipientUniqueId = 0; // 接收方UserID
	private Integer messageUniqueId = 0; // 消息ID
	private Integer markedAsRead = 0; // 标记已读
	private String viewName; // 视图名称，如果等于空，则表示发送到对方的首个view上。具体请参考DashboardViewType类
}
