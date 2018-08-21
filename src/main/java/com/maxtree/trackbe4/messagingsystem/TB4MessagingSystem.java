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
import com.maxtree.automotive.dashboard.domain.SendDetails;
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
	 * @param messageBody
	 * @return
	 */
	public Message createNewMessage(User creator, String subject, String messageBody) {
		Message newMessage = new Message();
		newMessage.setCreatorUniqueId(creator.getUserUniqueId());
		newMessage.setSubject(subject);
		newMessage.setMessageBody(messageBody);
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
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				// 一个用户只发送一次
    				if (!sent.contains(u.getUserUniqueId())) {
    					SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
        				list.add(sd);
        				
        				sent.add(u.getUserUniqueId());
    				}
    				
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType() == Name.COMPANY) {
    			mr = name2MessageRecipient(n, Name.COMPANY, messageUniqueId);
    			
    			int companyUniqueId = n.getUniqueId();
    			List<User> users = ui.companyService.findAllUsers(companyUniqueId);
    			// details
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				if (!sent.contains(u.getUserUniqueId())) {
    					SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
        				list.add(sd);
        				
        				sent.add(u.getUserUniqueId());
    				}
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType() == Name.USER) {
    			mr = name2MessageRecipient(n, Name.USER, messageUniqueId);
    			
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			User u = ui.userService.findById(n.getUniqueId());
    			
    			if (!sent.contains(u.getUserUniqueId())) {
    				SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
    				list.add(sd);
    				
    				sent.add(u.getUserUniqueId());
    			}
				ui.messagingService.insertSendDetails(list);
				
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
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType() == Name.COMPANY) {
    			mr = name2MessageRecipient(n, Name.COMPANY, messageUniqueId);
    			
    			int companyUniqueId = n.getUniqueId();
    			List<User> users = ui.companyService.findAllUsers(companyUniqueId);
    			// details
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType() == Name.USER) {
    			mr = name2MessageRecipient(n, Name.USER, messageUniqueId);
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			
    			User u = ui.userService.findById(n.getUniqueId());
    			SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
				list.add(sd);
				ui.messagingService.insertSendDetails(list);
    		}
		}
	}
	
	/**
	 * 即删除接收者又删除消息。
	 * 
	 * @param messageUniqueId
	 * @param recipientUniqueId
	 */
	public void deleteMessage(int messageUniqueId, int recipientUniqueId) {
		ui.messagingService.deleteSendDetails(messageUniqueId,recipientUniqueId);
		ui.messagingService.deleteMessageRecipient(messageUniqueId,recipientUniqueId);
		ui.messagingService.deleteMessage(messageUniqueId);
		
		CacheManager.getInstance().getSendDetailsCache().refresh(recipientUniqueId);
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param recipient
	 * @param viewName
	 * @return
	 */
	private SendDetails user2SendDetails(int messageUniqueId, User recipient, String viewName) {
		SendDetails sd = new SendDetails();
		sd.setMessageUniqueId(messageUniqueId);
		sd.setRecipientUniqueId(recipient.getUserUniqueId());
		sd.setMarkedAsRead(0);
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
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
