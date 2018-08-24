package com.maxtree.automotive.dashboard.view.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MessageInboxWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param allMessages
	 */
	public MessageInboxWindow(List<Map<String, Object>> allMessages) {
		this.setCaption("消息列表");
		this.setWidth("1105px");
		this.setHeightUndefined();
		this.setResizable(true);
		this.setClosable(true);
		this.setModal(true);
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		for(Map<String, Object> m : allMessages) {
			int messageUniqueId = Integer.parseInt(m.get("messageuniqueid").toString());
			String senderUserName = m.get("username").toString();
			String senderPicture = "VAADIN/themes/dashboard/"+ m.get("picture").toString();
			String subject = m.get("subject").toString();
			String json = m.get("messagebody").toString();
			String comments = m.get("comments").toString();
			Map<String, String> map = new MessageBodyParser().json2Map(json);
			String status = map.get("1").toString();
			String plateType = map.get("3").toString();
			String uuid = map.get("7").toString();
			StringBuilder content = new StringBuilder();
			content.append("状态："+map.get("1")+"\n");
			content.append("条形码："+map.get("2")+"\n");
			content.append("号牌种类："+map.get("3")+"\n");
			content.append("车牌号："+map.get("4")+"\n");
			content.append("车辆识别代码："+map.get("5")+"\n");
			content.append("业务类型："+map.get("6")+"\n");
			content.append("备注："+comments+"\n");
			String read = m.get("markedasread").toString().equals("1")?"已读":"未读";
			Date dateCreated = (Date) m.get("datecreated");
			MessageWrapper wrapper = new MessageWrapper(messageUniqueId, senderPicture+" "+senderUserName, senderPicture, subject, content.toString(), uuid, read, dateCreated, plateType, status);
			messageWrapperList.add(wrapper);
		}
		initComponents();
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		// Create new Grid of bug entries and set a ListDataProvider that
        // provides BugEntries
		grid.removeAllColumns();
		grid.setWidth("100%");
		grid.setHeight("400px");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setItems(messageWrapperList);
    	grid.setHeightByRows(5);
    	grid.addColumn(MessageWrapper::getSenderUserName).setCaption("收自").setRenderer(new ImageNameRenderer());
    	grid.addColumn(MessageWrapper::getSubject).setCaption("标题");
    	grid.addColumn(MessageWrapper::getMessage).setCaption("内容");
    	grid.addColumn(MessageWrapper::getDateCreated).setCaption("创建时间");
    	grid.addColumn(MessageWrapper::getRead).setCaption("是否阅读");
    	
    	grid.addSelectionListener(event -> {
    		
    		Set<MessageWrapper> selected = event.getAllSelectedItems();
    	    if (selected.size() > 0) {
    	    	List<MessageWrapper> selectreminders = new ArrayList<>(selected);
        	    textarea.setValue(selectreminders.get(0).getMessage()==null?"":selectreminders.get(0).getMessage());
        	    // 标记选中记录为已读
        	    User loginUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        	    ui.messagingService.markAsRead(selectreminders.get(0).getMessageUniqueId(), loginUser.getUserUniqueId());
        	    selectreminders.get(0).setRead("已读");
        	    // 刷新提示数量
        	    updateUnreadEvent.onSuccessful();
        	    
        	    // 刷新缓存
        	    CacheManager.getInstance().getSendDetailsCache().refresh(loginUser.getUserUniqueId());
        	    
    	    } 
    	});
    	
    	textarea.setWidth("98%");
    	textarea.setHeight("200px");
    	
    	HorizontalLayout buttonLayout = new HorizontalLayout();
//    	buttonLayout.addStyleName("MessageInboxWindow");
    	buttonLayout.setSpacing(false);
    	buttonLayout.setMargin(false);
    	buttonLayout.setWidthUndefined();
    	buttonLayout.setHeight("40px");
    	buttonLayout.addComponents(btnDelete, Box.createHorizontalBox(5), btnQuit, Box.createHorizontalBox(5));
    	buttonLayout.setComponentAlignment(btnDelete, Alignment.TOP_LEFT);
    	buttonLayout.setComponentAlignment(btnQuit, Alignment.TOP_LEFT);
    	
    	VerticalLayout vlayout = new VerticalLayout();
		vlayout.setSpacing(false);
		vlayout.setMargin(false);
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
    	vlayout.addComponents(grid, Box.createVerticalBox(1), textarea, Box.createVerticalBox(1), buttonLayout);
    	vlayout.setComponentAlignment(grid, Alignment.TOP_CENTER);
    	vlayout.setComponentAlignment(textarea, Alignment.TOP_CENTER);
    	vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
        
    	this.setContent(vlayout);
    	
    	initActions();
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 */
	private void setSelectedRow(int messageUniqueId) {
		for (MessageWrapper wrapper : messageWrapperList) {
			if(wrapper.getMessageUniqueId() == messageUniqueId) {
				grid.select(wrapper);
				break;
			}
		}
	}
	
	/**
	 * 
	 */
	private void initActions() {
		btnDelete.addClickListener(e-> {
			Set<MessageWrapper> selected = grid.getSelectedItems();
			if (selected.isEmpty()) {
				Notifications.warning("请选择一行记录。");
				return;
			}
			Iterator<MessageWrapper> iter = selected.iterator();
			while (iter.hasNext()) {
				MessageWrapper messageWrapper = iter.next();
				int messageUniqueId = messageWrapper.getMessageUniqueId();
				ui.messagingService.deleteSendDetails(messageUniqueId,loggedInUser.getUserUniqueId());
				ui.messagingService.deleteMessageRecipient(messageUniqueId,loggedInUser.getUserUniqueId());
				ui.messagingService.deleteMessage(messageUniqueId);
				
				CacheManager.getInstance().getSendDetailsCache().refresh(loggedInUser.getUserUniqueId());
				
				messageWrapperList.remove(messageWrapper);
				grid.setItems(messageWrapperList);
				textarea.setValue("");
			}
		});
		btnQuit.addClickListener(e->{
			close();
		});
	}
	
	/**
	 * 
	 * @param allMessages
	 * @param updateUnreadEvent
	 * @param selectedMessageUniqueId
	 */
	public static void open(List<Map<String, Object>> allMessages, Callback2 updateUnreadEvent, int selectedMessageUniqueId) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        MessageInboxWindow w = new MessageInboxWindow(allMessages);
        w.updateUnreadEvent = updateUnreadEvent;
        if (selectedMessageUniqueId > 0) {
        	w.setSelectedRow(selectedMessageUniqueId);
        }
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private User loggedInUser;
	private Callback2 updateUnreadEvent;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Grid<MessageWrapper> grid = new Grid<>(MessageWrapper.class);
	private TextArea textarea = new TextArea();
	private List<MessageWrapper> messageWrapperList = new ArrayList<MessageWrapper>();
	private Button btnDelete = new Button("删除");
	private Button btnQuit = new Button("关闭");
}
