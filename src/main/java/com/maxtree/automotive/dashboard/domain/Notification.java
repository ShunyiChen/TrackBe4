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
	
	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getRelativeTime() {
		return relativeTime;
	}

	public void setRelativeTime(Date relativeTime) {
		this.relativeTime = relativeTime;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return String.format(
				"Notification[notificationUniqueId=%d, warning='%s' content='%s',relativeTime='%s',checked='%d',userName='%d']",
				notificationUniqueId, warning, content, relativeTime, checked, userName);
	}
	
	private Integer notificationUniqueId = 0; //自增ID
	private boolean warning;//警告图标
	private String content;//通知内容
	private Date relativeTime;//相对时间
	private boolean checked;//是否已查阅
	private String userName;//被通知人
}
