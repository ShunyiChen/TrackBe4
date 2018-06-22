package com.maxtree.trackbe4.messagingsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
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
	
	public void sendMessageTo(int messageUniqueId, List<Name> names, String viewName) {
		
		List<MessageRecipient> recipients = new ArrayList<MessageRecipient>();
		for (Name n : names) {
			MessageRecipient mr = null;
    		if (n.getType().equals("community")) {
    			mr = name2MessageRecipient(n, 1, messageUniqueId);
    			
    			int communityUniqueId = n.getUniqueId();
    			List<User> users = ui.communityService.findAllUsers(communityUniqueId);
    			// details
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType().equals("company")) {
    			mr = name2MessageRecipient(n, 2, messageUniqueId);
    			
    			int companyUniqueId = n.getUniqueId();
    			List<User> users = ui.companyService.findAllUsers(companyUniqueId);
    			// details
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			for (User u : users) {
    				SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
    				list.add(sd);
    			}
    			ui.messagingService.insertSendDetails(list);
    			
    		} else if (n.getType().equals("user")) {
    			mr = name2MessageRecipient(n, 3, messageUniqueId);
    			
    			List<SendDetails> list = new ArrayList<SendDetails>();
    			User u = ui.userService.findById(n.getUniqueId());
    			SendDetails sd = user2SendDetails(messageUniqueId, u, viewName);
				list.add(sd);
				ui.messagingService.insertSendDetails(list);
				
    		}
    		recipients.add(mr);
		}
		
		ui.messagingService.insertMessageRecipients(recipients);
		
	}
	
	public void receiveMessage(User receiver) {
		
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
	
	/**
	 * 
	 * @param sender
	 * @param recipientName - recipient's recipientName
	 * @param communityUniqueId - recipient's communityUniqueId
	 * @param companyUniqueId - recipient's companyUniqueId
	 * @param userUniqueId - recipient's userUniqueId
	 * @param subject
	 * @param messageBody  json
	 */
	public int sendMessageTo(User sender, String recipientName, int communityUniqueId, int companyUniqueId, int userUniqueId, String subject, String messageBody, String viewName) {
		return 0;
//		Message newMessage = new Message();
//		newMessage.setCreatorUniqueId(sender.getUserUniqueId());
//		newMessage.setRecipientName(recipientName);
//		newMessage.setDateCreated(new Date());
//		newMessage.setExpiryDate(new Date());
//		newMessage.setNextRemindDate(new Date());
//		newMessage.setSubject(subject);
//		newMessage.setMessageBody(messageBody);
//		int messageUniqueId = ui.messagingService.createMessage(newMessage);
//		List<MessageRecipient> recipients = new ArrayList<MessageRecipient>();
//		if (communityUniqueId != 0) {
//			List<User> allUsers = ui.communityService.findAllUsers(communityUniqueId);
//			for (User user : allUsers) {
//				MessageRecipient recipient = new MessageRecipient();
//				recipient.setRecipientUniqueId(user.getUserUniqueId());
//				recipient.setCommunityUniqueId(communityUniqueId);
//				recipient.setMessageUniqueId(messageUniqueId);
//				recipient.setCompanyUniqueId(0);
//				recipient.setRead(0);
//				recipient.setViewName(viewName);
//				
//				recipients.add(recipient);
//			}
//			
//		} else if (companyUniqueId != 0) {
//			List<User> allUsers = ui.companyService.findAllUsers(companyUniqueId);
//			for (User user : allUsers) {
//				MessageRecipient recipient = new MessageRecipient();
//				recipient.setRecipientUniqueId(user.getUserUniqueId());
//				recipient.setCommunityUniqueId(0);
//				recipient.setMessageUniqueId(messageUniqueId);
//				recipient.setCompanyUniqueId(companyUniqueId);
//				recipient.setRead(0);
//				recipient.setViewName(viewName);
//				
//				recipients.add(recipient);
//			}
//		} else if (userUniqueId != 0) {
//			MessageRecipient recipient = new MessageRecipient();
//			recipient.setRecipientUniqueId(userUniqueId);
//			recipient.setCommunityUniqueId(0);
//			recipient.setMessageUniqueId(messageUniqueId);
//			recipient.setCompanyUniqueId(0);
//			recipient.setRead(0);
//			recipient.setViewName(viewName);
//			
//			recipients.add(recipient);
//		}
//		if (recipients.size() > 0)
//			ui.messagingService.createMessageRecipients(recipients);
//		return messageUniqueId;
	}
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
