package com.maxtree.trackbe4.messagingsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.ui.UI;

/**
 * 
 * @author chens
 *
 */
public class TB4MessagingSystem {
	
	/**
	 * 
	 * @param creator
	 * @param subject
	 * @param content
	 * @param matedata
	 * @return
	 */
	public Message createNewMessage(User creator, String subject, String content, String matedata) {
		Message newMessage = new Message();
		newMessage.setCreatorUniqueId(creator.getUserUniqueId());
		newMessage.setSubject(subject);
		newMessage.setContent(content);
		newMessage.setMatedata(matedata);
		newMessage.setSentTimes(1);
		newMessage.setReminderFrequencyId(0);
		int messageUniqueId = ui.messagingService.insertMessage(newMessage);
		newMessage.setMessageUniqueId(messageUniqueId);
		return newMessage;
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param names
	 * @param viewName
	 */
	public void sendMessageTo(int messageUniqueId, Set<Name> names, String viewName) {
		List<Integer> sent = new ArrayList<>();
		List<MessageRecipient> recipients = new ArrayList<MessageRecipient>();
		for (Name n : names) {
			MessageRecipient mr = null;
    		if (n.getType() == Name.COMMUNITY) {
    			mr = name2MessageRecipient(n, Name.COMMUNITY, messageUniqueId);
    			
    			int communityUniqueId = n.getUniqueId();
    			List<User> users = ui.communityService.findAllUsers(communityUniqueId);
    			// details
    			List<Notification> list = new ArrayList<>();
    			for (User u : users) {
    				// 一个用户只发送一次
    				if (!sent.contains(u.getUserUniqueId())) {
    					Notification sd = user2Notification(messageUniqueId, u, viewName);
        				list.add(sd);
        				
        				sent.add(u.getUserUniqueId());
    				}
    				
    			}
    			ui.messagingService.insertNotifications(list);
    			
    		} else if (n.getType() == Name.COMPANY) {
    			mr = name2MessageRecipient(n, Name.COMPANY, messageUniqueId);
    			
    			int companyUniqueId = n.getUniqueId();
    			List<User> users = ui.companyService.findAllUsers(companyUniqueId);
    			// details
    			List<Notification> list = new ArrayList<>();
    			for (User u : users) {
    				if (!sent.contains(u.getUserUniqueId())) {
    					Notification sd = user2Notification(messageUniqueId, u, viewName);
        				list.add(sd);
        				
        				sent.add(u.getUserUniqueId());
    				}
    			}
    			ui.messagingService.insertNotifications(list);
    			
    		} else if (n.getType() == Name.USER) {
    			mr = name2MessageRecipient(n, Name.USER, messageUniqueId);
    			
    			List<Notification> list = new ArrayList<>();
    			User u = ui.userService.findById(n.getUniqueId());
    			if (!sent.contains(u.getUserUniqueId())) {
    				Notification sd = user2Notification(messageUniqueId, u, viewName);
    				list.add(sd);
    				
    				sent.add(u.getUserUniqueId());
    			}
				ui.messagingService.insertNotifications(list);
				
    		}
    		recipients.add(mr);
		}
		ui.messagingService.insertMessageRecipients(recipients);
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param names
	 * @param viewName
	 */
	public void resendMessageTo(int messageUniqueId, Set<Name> names, String viewName, DashboardUI ui) {
		for (Name n : names) {
			MessageRecipient mr = null;
    		if (n.getType() == Name.COMMUNITY) {
    			mr = name2MessageRecipient(n, Name.COMMUNITY, messageUniqueId);
    			
    			int communityUniqueId = n.getUniqueId();
    			List<User> users = ui.communityService.findAllUsers(communityUniqueId);
    			// details
    			List<Notification> list = new ArrayList<>();
    			for (User u : users) {
    				Notification sd = user2Notification(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertNotifications(list);
    			
    		} else if (n.getType() == Name.COMPANY) {
    			mr = name2MessageRecipient(n, Name.COMPANY, messageUniqueId);
    			
    			int companyUniqueId = n.getUniqueId();
    			List<User> users = ui.companyService.findAllUsers(companyUniqueId);
    			// details
    			List<Notification> list = new ArrayList<Notification>();
    			for (User u : users) {
    				Notification sd = user2Notification(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertNotifications(list);
    			
    		} else if (n.getType() == Name.USER) {
    			mr = name2MessageRecipient(n, Name.USER, messageUniqueId);
    			List<Notification> list = new ArrayList<Notification>();
    			
    			User u = ui.userService.findById(n.getUniqueId());
    			Notification sd = user2Notification(messageUniqueId, u, viewName);
				list.add(sd);
				ui.messagingService.insertNotifications(list);
    		}
		}
	}
	
	/**
	 * 删除消息
	 * 
	 * @param messageUniqueId
	 * @param recipientUniqueId
	 * @param deleteType （MARKASDELETED：删除接收者并标识Message为删除状态，PERMANENTLYDELETE:删除接收者并永久删除Message,ONLYDELETERECIPIENT:只删除接收者）
	 */
	public void deleteMessage(int messageUniqueId, int recipientUniqueId, int deleteType) {
		ui.messagingService.deleteNotifications(messageUniqueId,recipientUniqueId);
		ui.messagingService.deleteMessageRecipient(messageUniqueId,recipientUniqueId);
		if (deleteType == MARKASDELETED) {
			ui.messagingService.markAsDeleted(messageUniqueId);
		}
		else if(deleteType == PERMANENTLYDELETE) {
			ui.messagingService.permanentlyDeleteMessage(messageUniqueId);
		}
		CacheManager.getInstance().getNotificationsCache().refresh(recipientUniqueId);
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param recipient
	 * @param viewName
	 * @return
	 */
	private Notification user2Notification(int messageUniqueId, User recipient, String viewName) {
		Notification sd = new Notification();
		sd.setMessageUniqueId(messageUniqueId);
		sd.setUserUniqueId(recipient.getUserUniqueId());
		sd.setMarkedAsRead(false);
		sd.setViewName(viewName);
		return sd;
	}
	
	/**
	 * 
	 * @param n
	 * @param recipientType
	 * @param messageUniqueId
	 * @return
	 */
	private MessageRecipient name2MessageRecipient(Name n, int recipientType, int messageUniqueId) {
		MessageRecipient mr = new MessageRecipient();
		mr.setRecipientUniqueId(n.getUniqueId());
		mr.setRecipientName(n.getName());
		mr.setRecipientType(recipientType);
		mr.setMessageUniqueId(messageUniqueId);
		return mr;
	}
	
	// 定时发送消息
    public static Map<Integer, Timer> SCHEDULED = new HashMap<Integer, Timer>();
	public static final int MARKASDELETED = 1;
	public static final int PERMANENTLYDELETE = 2;
	public static final int ONLYDELETERECIPIENT = 3;
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
