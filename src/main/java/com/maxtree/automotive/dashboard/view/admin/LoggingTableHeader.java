package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author Chen
 *
 */
public class LoggingTableHeader extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoggingTableHeader() {
		this.setWidthUndefined();
		this.setHeight("30px");
		this.setSpacing(false);
		this.setMargin(false);
		Label dateCreated = new Label("日期");
		Label type = new Label("类型");
		Label userName = new Label("用户名");
		Label ipAddr = new Label("IP地址");
		Label module = new Label("模块");
		Label message = new Label("消息");
		Label opt = new Label("操作");
 
		dateCreated.setWidth("200px");
		type.setWidth("60px");
		userName.setWidth("100px");
		ipAddr.setWidth("200px");
		module.setWidth("200px");
		message.setWidth("200px");
		opt.setWidth("60px");
		
		this.addComponents(dateCreated,type,userName,ipAddr,module,message,opt);
		this.addStyleName("LoggingTableHeader");
		this.setComponentAlignment(dateCreated, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(type, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(ipAddr, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(module, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(message, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(opt, Alignment.MIDDLE_LEFT);
	}
}
