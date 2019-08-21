package com.maxtree.automotive.dashboard.domain;

/**
 * 站点
 * 
 * @author chens
 *
 */
public class Site {

	public Integer getSiteUniqueId() {
		return siteUniqueId;
	}

	public void setSiteUniqueId(Integer siteUniqueId) {
		this.siteUniqueId = siteUniqueId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Integer getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(Integer runningStatus) {
		this.runningStatus = runningStatus;
	}
	
	public SiteCapacity getSiteCapacity() {
		return siteCapacity;
	}

	public void setSiteCapacity(SiteCapacity siteCapacity) {
		this.siteCapacity = siteCapacity;
	}
	
	@Override
	public String toString() {
		// return String.format(
		// "Site[siteUniqueId=%d, siteName='%s', siteType='%s', hostAddr='%s', port=%d,
		// defaultRemoteDirectory='%s', userName='%s', password='%s', mode='%s',
		// charset='%s', runningStatus=%d]",
		// siteUniqueId, siteName, siteType, hostAddr, port, defaultRemoteDirectory,
		// userName, password, mode, charset, runningStatus);
		return siteName;
	}

	private Integer siteUniqueId = 0; 		// 自增长ID
	private String siteName; 				// 站点名称
	private String siteType; 				// 站点类型
	private String hostAddr; 				// 主机地址
	private Integer port = 0; 				// 端口号
	private String defaultRemoteDirectory = "/"; // 默认远程目录
	private String userName; 				// 用户名
	private String password; 				// 密码
	private String mode = "主动"; 			// 传输模式（主动，被动）
	private String charset = "UTF-8"; 		// 字符集，默认UTF-8
	private Integer runningStatus = 0; 		// 运行状态 1运行  0停止
	private SiteCapacity siteCapacity; 		// 数据容量及文件夹个数限制
}
