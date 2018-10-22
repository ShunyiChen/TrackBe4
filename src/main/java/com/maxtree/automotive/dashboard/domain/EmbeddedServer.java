package com.maxtree.automotive.dashboard.domain;

/**
 * 
 * @author Chen
 *
 */
public class EmbeddedServer {

	public Integer getServerUniqueId() {
		return serverUniqueId;
	}

	public void setServerUniqueId(Integer serverUniqueId) {
		this.serverUniqueId = serverUniqueId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDefaultRemoteDirectory() {
		return defaultRemoteDirectory;
	}

	public void setDefaultRemoteDirectory(String defaultRemoteDirectory) {
		this.defaultRemoteDirectory = defaultRemoteDirectory;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(Integer runningStatus) {
		this.runningStatus = runningStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		// return String.format(
		// "Site[siteUniqueId=%d, siteName='%s', siteType='%s', hostAddr='%s', port=%d,
		// defaultRemoteDirectory='%s', userName='%s', password='%s', mode='%s',
		// charset='%s', runningStatus=%d]",
		// siteUniqueId, siteName, siteType, hostAddr, port, defaultRemoteDirectory,
		// userName, password, mode, charset, runningStatus);
		return serverName;
	}

	private Integer serverUniqueId = 0; // 自增长ID
	private String serverName; // 站点名称
	private String serverType; // 站点类型
	private String hostAddr; // 主机地址
	private Integer port = 0; // 端口号
	private String defaultRemoteDirectory = "/"; // 默认远程目录
	private String userName; // 用户名
	private String password; // 密码
	private Integer runningStatus = 0; // 运行状态 1运行 0停止
	private String code; // 快捷编码
}
