package com.maxtree.automotive.dashboard.view.admin;

import java.text.SimpleDateFormat;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Log;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author Chen
 *
 */
public class LoggingTableRow extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param log
	 */
	public LoggingTableRow(final Log log) {
		this.setWidthUndefined();
		this.setHeightUndefined();
		this.setSpacing(false);
		this.setMargin(false);
		
		Label dateCreated = new Label();
		Image type = null;
		if(log.getLogType().equals("Info")) {
			type = new Image(null, new ThemeResource("img/logging/info-circle-o.png"));
		}
		else if(log.getLogType().equals("Debug")) {
			type = new Image(null, new ThemeResource("img/logging/warning.png"));
		}
		else if(log.getLogType().equals("Error")) {
			type = new Image(null, new ThemeResource("img/logging/bug-o.png"));
		}
		else if(log.getLogType().equals("Trace")) {
			type = new Image(null, new ThemeResource("img/logging/warning.png"));
		}
		
		Label userName = new Label();
		Label ipAddr = new Label();
		Label module = new Label();
		Label message = new Label();
		Image menu = new Image(null, new ThemeResource("img/logging/ellipsis-h.png"));
		menu.addStyleName("LoggingTableRow_menu");
		menu.addClickListener(e->{
			ContextMenu cmenu = new ContextMenu(menu, true);
			cmenu.addItem("查看异常", new Command() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
//					if (loggedInUser.isPermitted(PermissionCodes.G4)) {
//						 
//					}
//					else {
//		        		Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
//		        	}
					ExceptionViewer.open(log);
				}
			});
			cmenu.open(e.getClientX(), e.getClientY());
		});
		
		dateCreated.setWidth("200px");
		dateCreated.setHeightUndefined();
		userName.setWidth("100px");
		userName.setHeightUndefined();
		ipAddr.setWidth("200px");
		ipAddr.setHeight("32px");
		module.setWidth("200px");
		module.setHeightUndefined();
		message.setWidth("210px");
		message.setHeightUndefined();
//		menu.setWidth("60px");
//		menu.setHeightUndefined();
		
		dateCreated.setValue(dateFormat.format(log.getDateCreated()));
		userName.setValue(log.getUserName());
		ipAddr.setValue(log.getIpAddr());
		module.setValue(log.getModule());
		message.setValue(log.getMessage());
		
		this.addComponents(dateCreated,type,Box.createHorizontalBox(40),userName,ipAddr,module,message,menu,Box.createHorizontalBox(20));
		
		this.setComponentAlignment(dateCreated, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(type, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(ipAddr, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(module, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(message, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(menu, Alignment.MIDDLE_LEFT);
	}
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
