package com.maxtree.automotive.dashboard.view.admin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Box;
//import com.maxtree.automotive.dashboard.domain.Message;
//import com.maxtree.automotive.dashboard.domain.MessageRecipient;
//import com.maxtree.automotive.dashboard.domain.ReminderFrequency;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.trackbe4.messagingsystem.Name;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class FrequencySettingsWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 */
//	public FrequencySettingsWindow() {
//
//		initComponents();
//	}
//
//	/**
//	 *
//	 */
//	private void initComponents() {
//		this.setWidth("400px");
//		this.setHeightUndefined();
//		this.setModal(true);
//		this.setResizable(false);
//		this.setCaption("设置发送频率");
////		this.addStyleName("edit-window");
//		VerticalLayout mainLayout = new VerticalLayout();
////		mainLayout.setSpacing(false);
////		mainLayout.setMargin(false);
//		FormLayout form = new FormLayout();
//		form.setSpacing(false);
//		form.setMargin(false);
//		form.setSizeFull();
//		form.addComponents(titleField, frequencyBox, enableBox, startDate, expiryDate);
//
//		String[] items = {"每天","每周"};
//		frequencyBox.setItems(items);
//		frequencyBox.setTextInputAllowed(false);
//		frequencyBox.setEmptySelectionAllowed(false);
//		frequencyBox.setSelectedItem("每天");
//
//		String[] items2 = {"启用","禁用"};
//		enableBox.setItems(items2);
//		enableBox.setTextInputAllowed(false);
//		enableBox.setEmptySelectionAllowed(false);
//		enableBox.setSelectedItem("启用");
//
//		titleField.setValue("计划_1");
//		titleField.setWidth("100%");
//		titleField.setHeight("27px");
//		frequencyBox.setWidth("100%");
//		frequencyBox.setHeight("27px");
//		enableBox.setWidth("100%");
//		enableBox.setHeight("27px");
//
//		startDate.setWidth("100%");
//		startDate.setHeight("27px");
//
//		expiryDate.setWidth("100%");
//		expiryDate.setHeight("27px");
//
//		HorizontalLayout buttonPane = new HorizontalLayout();
//		buttonPane.setSizeFull();
//		buttonPane.setSpacing(false);
//		buttonPane.setMargin(false);
//		HorizontalLayout subButtonPane = new HorizontalLayout();
//		subButtonPane.setSpacing(false);
//		subButtonPane.setMargin(false);
//		subButtonPane.setWidthUndefined();;
//		subButtonPane.setHeight("100%");
//		subButtonPane.addComponents(btnCancel, Box.createHorizontalBox(5), btnOk);
//		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
//		subButtonPane.setComponentAlignment(btnOk, Alignment.BOTTOM_LEFT);
//		buttonPane.addComponent(subButtonPane);
//		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
//
//		mainLayout.addComponents(form, Box.createVerticalBox(5), buttonPane);
//		this.setContent(mainLayout);
//
//		btnCancel.addClickListener(e -> {
//			close();
//		});
//
//	}
//
//	public static void open(Message message, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        FrequencySettingsWindow w = new FrequencySettingsWindow();
//        if (message.getReminderFrequencyId() != 0) {
//        	ReminderFrequency rf = ui.messagingService.findByFrequencyId(message.getReminderFrequencyId());
//        	w.titleField.setValue(rf.getName());
//        	w.frequencyBox.setSelectedItem(rf.getFrequency() == 1?"每天":"每周");
//        	w.enableBox.setSelectedItem(rf.getEnabled() == 1?"启用":"禁用");
//
//        	LocalDateTime startDatetime = LocalDateTime.ofInstant(rf.getStartingTime().toInstant(), ZoneId.systemDefault());
//        	w.startDate.setValue(startDatetime);
//        	LocalDateTime endDatetime = LocalDateTime.ofInstant(rf.getEndingTime().toInstant(), ZoneId.systemDefault());
//        	w.expiryDate.setValue(endDatetime);
//        }
//        w.btnOk.addClickListener(e -> {
//        	int reminderFrequencyId = message.getReminderFrequencyId();
//        	ReminderFrequency frequencry = new ReminderFrequency();
//        	frequencry.setName(w.titleField.getValue());
//        	frequencry.setFrequency(w.frequencyBox.getValue().equals("每天")?1:7);
//        	frequencry.setEnabled(w.enableBox.getValue().equals("启用")?1:0);
//        	frequencry.setFrequencyUniqueId(reminderFrequencyId);
//
//        	ZonedDateTime zdt1 = w.startDate.getValue().atZone(ZoneId.systemDefault());
//        	Date startDateTime = Date.from(zdt1.toInstant());
//
//        	ZonedDateTime zdt2 = w.expiryDate.getValue().atZone(ZoneId.systemDefault());
//        	Date endDateTime = Date.from(zdt2.toInstant());
//
//        	frequencry.setStartingTime(startDateTime);
//        	frequencry.setEndingTime(endDateTime);
//
//        	Date nextDate = frequencry.getStartingTime();// new Date(); // 取下一次提醒时间
//        	long period = 0;
//			if (w.frequencyBox.getValue().equals("每天")) {
////				Calendar calendar = new GregorianCalendar();
////				calendar.setTime(nextDate);
////				calendar.add(calendar.DATE, 1); // 把日期往后增加一天.整数往后推,负数往前移动
////				nextDate = calendar.getTime();
//
//				period = 24 * 60 * 60 * 1000;// 一天毫秒数
//
//			} else if(w.frequencyBox.getValue().equals("每周")){
////				Calendar calendar = new GregorianCalendar();
////				calendar.setTime(nextDate);
////				calendar.add(calendar.DATE, 7);
////				nextDate = calendar.getTime();
//
//				period = 7L * 24L * 60L * 60L * 1000L;// 一周毫秒数
//
//			}
//        	// 更新
//        	if (reminderFrequencyId != 0) {
//            	ui.messagingService.updateReminderFrequency(frequencry);
//
//            	Timer oldTimer = TB4MessagingSystem.SCHEDULED.get(message.getMessageUniqueId());
//            	oldTimer.cancel();
//            	w.scheduleNew(message.getMessageUniqueId(), (frequencry.getEnabled() == 1), nextDate, frequencry.getEndingTime(), period);
//            }
//        	// 插入新值
//        	else {
//            	reminderFrequencyId = ui.messagingService.insertReminderFrequency(frequencry);
//            	w.scheduleNew(message.getMessageUniqueId(), (frequencry.getEnabled() == 1), nextDate, frequencry.getEndingTime(), period);
//
//            	message.setReminderFrequencyId(reminderFrequencyId);
//            	ui.messagingService.updateMessage(message);
//
//            }
//			w.close();
//		});
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	/**
//	 *
//	 * @param messageUniqueId
//	 * @param enabled
//	 * @param firstTime
//	 * @param expiryDate
//	 * @param period
//	 */
//	private void scheduleNew(int messageUniqueId, boolean enabled, Date firstTime, Date expiryDate, long period) {
//		Timer newTimer = new Timer();
//    	TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				// re-send message
//				LinkedHashSet<Name> nameSets = new LinkedHashSet<Name>();
//				List<MessageRecipient> list = ui.messagingService.findRecipientsByMessageId(messageUniqueId);
//		        for (MessageRecipient mr : list) {
//		        	Name n = new Name(mr.getRecipientUniqueId(), mr.getRecipientType(), mr.getRecipientName(), "");
//		        	nameSets.add(n);
//		        }
//				new TB4MessagingSystem().resendMessageTo(messageUniqueId, nameSets, "", ui);
//
//				if (!enabled || System.currentTimeMillis() > expiryDate.getTime()) {
//					newTimer.cancel();
//				}
//			}
//    	};
//    	newTimer.schedule(task, firstTime, period);
//    	// 注册
//    	TB4MessagingSystem.SCHEDULED.put(messageUniqueId, newTimer);
//
//
//
////
////		Timer newTimer2 = new Timer();
////		TimerTask task2 = new TimerTask() {
////			@Override
////			public void run() {
////				System.out.println("ssssssssssssssssssssss");
////
////			}
////		};
////		newTimer2.schedule(task2, firstTime, 1000);
//
//	}
//
//	private TextField titleField = new TextField("标题:");
//	private ComboBox<String> frequencyBox = new ComboBox<String>("频率:");
//	private ComboBox<String> enableBox = new ComboBox<String>("是否启用:");
//	private DateTimeField startDate = new DateTimeField("起始时间:", LocalDateTime.now());
//	private DateTimeField expiryDate = new DateTimeField("结束时间:", LocalDateTime.now());
//	private Button btnCancel = new Button("取消");
//	private Button btnOk = new Button("确定");
//	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
