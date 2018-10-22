package com.maxtree.automotive.dashboard.domain;

/**
 * 批改建议
 * 
 * @author Chen
 *
 */
public class CorrectingSuggestions {

	public Integer getSuggestionUniqueId() {
		return suggestionUniqueId;
	}

	public void setSuggestionUniqueId(Integer suggestionUniqueId) {
		this.suggestionUniqueId = suggestionUniqueId;
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
		return String.format("CorrectingSuggestions[problemUniqueId=%d, userName='%s', problem='%s'， frequency=%d]",
				suggestionUniqueId, userName, suggestion);
	}

	private Integer suggestionUniqueId = 0; // UniqueID
	private String userName;// 用户名
	private String suggestion;// 批改建议
	private Integer frequency = 0;// 使用频率
}
