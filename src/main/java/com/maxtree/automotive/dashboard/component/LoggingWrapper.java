package com.maxtree.automotive.dashboard.component;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.domain.Log;
import com.maxtree.automotive.dashboard.service.LoggingService;
import com.maxtree.automotive.dashboard.service.TransactionService;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

/**
 * 
 * @author Chen
 *
 */
public class LoggingWrapper {
	
	private Logger logger;
	
	public LoggingWrapper(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}
	
	public void info(String userName,String module,String msg) {
		WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
		Log log = new Log();
		log.setLogType("Info");
		log.setMessage(msg);
		log.setUserName(userName);
		log.setIpAddr(webBrowser.getAddress());
		log.setModule(module);
		log.setDateCreated(new Date());
		logger.info(log.getMessage());
		TB4Application.getInstance().loggingService.insert(log);
	}
	
	public void debug(Log log) {
		log.setLogType("Debug");
		logger.debug(log.getMessage());
		TB4Application.getInstance().loggingService.insert(log);
	}
	
	public void error(Log log) {
		log.setLogType("Error");
		logger.error(log.getMessage());
		TB4Application.getInstance().loggingService.insert(log);
	}
	
	public void trace(Log log) {
		log.setLogType("Trace");
		logger.trace(log.getMessage());
		TB4Application.getInstance().loggingService.insert(log);
	}
	
	public static String LOGIN = "登录";
}
