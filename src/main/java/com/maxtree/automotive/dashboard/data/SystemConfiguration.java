package com.maxtree.automotive.dashboard.data;

public class SystemConfiguration {

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
	}

	public int getExpireAfterWrite() {
		return expireAfterWrite;
	}

	public void setExpireAfterWrite(int expireAfterWrite) {
		this.expireAfterWrite = expireAfterWrite;
	}

	public int getRefreshAfterWrite() {
		return refreshAfterWrite;
	}

	public void setRefreshAfterWrite(int refreshAfterWrite) {
		this.refreshAfterWrite = refreshAfterWrite;
	}

	public String getCreateDBTableOnStartup() {
		return createDBTableOnStartup;
	}

	public void setCreateDBTableOnStartup(String createDBTableOnStartup) {
		this.createDBTableOnStartup = createDBTableOnStartup;
	}

	private int interval; // UI事件轮询间隔（毫秒）
	private int maximumSize;// Caffeine缓存最大行数
	private int expireAfterWrite; // Caffeine缓存expireAfterWrite（分钟）
	private int refreshAfterWrite;// Caffeine缓存refreshAfterWrite（分钟）
	private String createDBTableOnStartup;//启动服务时新建数据表（可用参数yes/no）
	
}
