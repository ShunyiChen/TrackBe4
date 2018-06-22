package com.maxtree.automotive.dashboard.data;

public class SystemConfiguration {

	public int getPollinginterval() {
		return pollinginterval;
	}

	public void setPollinginterval(int pollinginterval) {
		this.pollinginterval = pollinginterval;
	}
	
	public int getRefreshcacheinterval() {
		return refreshcacheinterval;
	}

	public void setRefreshcacheinterval(int refreshcacheinterval) {
		this.refreshcacheinterval = refreshcacheinterval;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	private int pollinginterval; 	// 事件提醒轮询间隔为30秒
	private int refreshcacheinterval; //刷新权限缓存间隔为300秒
	private String dateformat;		// 打印结果单日期格式 yyyy年MM月dd日 HH:mm:SS
}
