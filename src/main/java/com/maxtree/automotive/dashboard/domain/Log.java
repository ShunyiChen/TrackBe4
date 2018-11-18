package com.maxtree.automotive.dashboard.domain;

import java.util.Date;

/**
 * 
 * @author Chen
 *
 */
public class Log {

	public Integer getLogUniqueId() {
		return logUniqueId;
	}

	public void setLogUniqueId(Integer logUniqueId) {
		this.logUniqueId = logUniqueId;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String toString() {
		return String.format(
				 "Log[logUniqueId=%d, logType='%s', userName='%s',ipAddr='%s',module='%s',message='%s',exception='%s',dateCreated='%s']",
				 logUniqueId,
				 logType,
				 userName,
				 ipAddr,
				 module,
				 message,
				 exception,
				 dateCreated
				);
	}

	private Integer logUniqueId = 0;
	private String logType;
	private String userName;
	private String ipAddr;
	private String module;
	private String message;
	private String exception;
	private Date dateCreated;
}
