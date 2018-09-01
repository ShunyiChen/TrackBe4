package com.maxtree.automotive.dashboard.view.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.Openwith;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Imaging;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
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
			String content = m.get("content").toString();
			String matedata = m.get("matedata").toString();
			String read = m.get("markedasread").toString().equals("1")?"已读":"未读";
			Date dateCreated = (Date) m.get("datecreated");
//			Map<String, String> matedataMap = new MessageBodyParser().json2Map(matedata);
			MessageWrapper wrapper = new MessageWrapper(messageUniqueId, senderPicture+" "+senderUserName, senderPicture, subject, content,matedata,read,dateCreated);
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
    	grid.addColumn(MessageWrapper::getContent).setCaption("内容");
    	grid.addColumn(MessageWrapper::getDateCreated).setCaption("创建时间");
    	grid.addColumn(MessageWrapper::getRead).setCaption("是否阅读");
    	
    	grid.addSelectionListener(event -> {
    		textarea.setValue("");
    		Set<MessageWrapper> selected = event.getAllSelectedItems();
    	    if (selected.size() > 0) {
    	    	List<MessageWrapper> list = new ArrayList<>(selected);
    	    	MessageWrapper msg = list.get(0);
    	    	
    	    	Map<String, String> matedataMap = new MessageBodyParser().json2Map(msg.getMatedata());
    	    	String openWith = matedataMap.get("openwith");
    	    	String imaginguniqueid = matedataMap.get("imaginguniqueid");
    	    	String uuid = matedataMap.get("uuid");
    	    	String vin = matedataMap.get("vin");
    	    	// 如果属性imaginguniqueid不为空
    	    	if(imaginguniqueid != null) {
    	    		Imaging img = ui.imagingService.findById(Integer.parseInt(imaginguniqueid));
    	    		
    	    		StringBuilder stb = new StringBuilder();
    	    		stb.append("申请人:"+img.getCreator()+"\n");
    	    		stb.append("申请时间:"+img.getDateCreated()+"\n");
    	    		stb.append("车牌号:"+img.getPlateNumber()+"\n");
    	    		stb.append("号牌种类:"+img.getPlateType()+"\n");
    	    		stb.append("车辆识别代码:"+img.getVin()+"\n");
    	    		stb.append("业务状态:"+img.getStatus());
    	    		textarea.setValue(stb.toString());
    	    		
    	    		ui.messagingService.markAsRead(msg.getMessageUniqueId(), loggedInUser.getUserUniqueId());
    	    		msg.setRead("已读");
    	    		// 刷新提示数量
            	    updateUnreadEvent.onSuccessful();
    	    	}
    	    	else if(uuid != null) {

    	    		textarea.setValue(msg.getContent());
    	    		
    	    		ui.messagingService.markAsRead(msg.getMessageUniqueId(), loggedInUser.getUserUniqueId());
    	    		msg.setRead("已读");
    	    		// 刷新提示数量
            	    updateUnreadEvent.onSuccessful();
    	    	}
    	    } 
    	});
    	FormLayout form = new FormLayout();
    	form.setSpacing(false);
    	form.setMargin(false);
    	form.setSizeFull();
    	form.addComponent(textarea);
    	textarea.setSizeFull();
    	
    	HorizontalLayout buttons = new HorizontalLayout();
    	buttons.setWidth("100%");
    	HorizontalLayout subbuttons = new HorizontalLayout();
    	subbuttons.addComponents(btnDelete, btnQuit);
    	buttons.addComponent(subbuttons);
    	buttons.setComponentAlignment(subbuttons, Alignment.MIDDLE_RIGHT);
    	
    	VerticalLayout main = new VerticalLayout();
    	main.setWidth("100%");
    	main.setHeightUndefined();
    	main.addComponents(grid,form,buttons);
    	main.setExpandRatio(grid, 1);
    	main.setExpandRatio(form, 1);
    	this.setContent(main);
    	
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
				
				MessageWrapper msg = iter.next();
				Map<String, String> matedataMap = jsonHelper.json2Map(msg.getMatedata());
				String openWith = matedataMap.get("openwith");
				
				// 如果发的是print和transaction打开方式，则有权彻底删除Messages表,因为一个消息只发一个人，即接收人可以永久删除message
				int deleteType = TB4MessagingSystem.ONLYDELETERECIPIENT;
				if(openWith.equals(Openwith.PRINT) || openWith.equals(Openwith.TRANSACTION)) {
					deleteType = TB4MessagingSystem.PERMANENTLYDELETE;
				}
				new TB4MessagingSystem().deleteMessage(msg.getMessageUniqueId(), loggedInUser.getUserUniqueId(), deleteType);
				
				messageWrapperList.remove(msg);
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
	
	private MessageBodyParser jsonHelper = new MessageBodyParser();
	private User loggedInUser;
	private Callback2 updateUnreadEvent;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private Grid<MessageWrapper> grid = new Grid<>(MessageWrapper.class);
	private TextArea textarea = new TextArea("详细信息:");
	private List<MessageWrapper> messageWrapperList = new ArrayList<MessageWrapper>();
	private Button btnDelete = new Button("删除");
	private Button btnQuit = new Button("关闭");
}
