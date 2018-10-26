package com.maxtree.automotive.dashboard.domain;

/**
 * 批改意见
 * 
 * @author Chen
 *
 */
public class Feedback {

	public Integer getFeedbackUniqueId() {
		return feedbackUniqueId;
	}

	public void setFeedbackUniqueId(Integer feedbackUniqueId) {
		this.feedbackUniqueId = feedbackUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
//		return String.format("Feedback[feedbackUniqueId=%d, userName='%s', suggestion='%s',frequency=%d]",
//				feedbackUniqueId, userName, suggestion,frequency);
		return suggestion;
	}

	private Integer feedbackUniqueId = 0; // UniqueID
	private String userName;// 用户名
	private String suggestion;// 批改意见
	private Integer frequency = 0;// 使用频率
}
