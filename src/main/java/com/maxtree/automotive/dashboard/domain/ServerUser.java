package com.maxtree.automotive.dashboard.domain;

/**
 * 
 * @author Chen
 *
 */
public class ServerUser {

	public Integer getServerUserUniqueId() {
		return serverUserUniqueId;
	}

	public void setServerUserUniqueId(Integer serverUserUniqueId) {
		this.serverUserUniqueId = serverUserUniqueId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Boolean getEnableflag() {
		return enableflag;
	}

	public void setEnableflag(Boolean enableflag) {
		this.enableflag = enableflag;
	}

	public Boolean getWritepermission() {
		return writepermission;
	}

	public void setWritepermission(Boolean writepermission) {
		this.writepermission = writepermission;
	}

	public Integer getMaxloginnumber() {
		return maxloginnumber;
	}

	public void setMaxloginnumber(Integer maxloginnumber) {
		this.maxloginnumber = maxloginnumber;
	}

	public Integer getMaxloginperip() {
		return maxloginperip;
	}

	public void setMaxloginperip(Integer maxloginperip) {
		this.maxloginperip = maxloginperip;
	}

	public Integer getIdletime() {
		return idletime;
	}

	public void setIdletime(Integer idletime) {
		this.idletime = idletime;
	}

	public Integer getUploadrate() {
		return uploadrate;
	}

	public void setUploadrate(Integer uploadrate) {
		this.uploadrate = uploadrate;
	}

	public Integer getDownloadrate() {
		return downloadrate;
	}

	public void setDownloadrate(Integer downloadrate) {
		this.downloadrate = downloadrate;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	private Integer serverUserUniqueId = 0;
	private String userName;
	private String userPassword;
	private Boolean enableflag = true;
	private Boolean writepermission = true;
	private Integer maxloginnumber = 0;
	private Integer maxloginperip = 0;
	private Integer idletime = 0;
	private Integer uploadrate = 0;
	private Integer downloadrate = 0;
	private String serverCode;
}
